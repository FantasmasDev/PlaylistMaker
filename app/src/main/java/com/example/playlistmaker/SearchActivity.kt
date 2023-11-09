package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.playlistmaker.TrackAdapter


class SearchActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    val userPreferences = UserPreferences()

    //адрес обращения
    private val baseURL = "https://itunes.apple.com"

    //Создаём ретрофит, создаём OkHTTP и перехватчик
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .build()

    private val itunesService = retrofit.create(itunesAPI::class.java)

    //Рабочее пространство
    private lateinit var binding: ActivitySearchBinding

    private lateinit var searchBar: EditText
    private lateinit var searchBarHint: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var cleanHistoryButton: Button

    private lateinit var historyPlaceHolder: LinearLayout

    private val trackList = ArrayList<Track>()

    private val trackAdapter = TrackAdapter {
        addTrackToHistory(it)
    }

    private val historyTrackAdapter = TrackAdapter {
        removeTrackFromHistory(it)
    }

    //сохраняем ввод
    private lateinit var lastSearchRequest: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //инициализация кнопок
        searchBar = binding.searchBar
        searchBarHint = binding.searchBarHint
        placeholderImage = binding.placeholderImage
        placeholderText = binding.placeholderText
        placeholderButton = binding.placeholderButton
        cleanHistoryButton = binding.cleanHistoryButton

        historyPlaceHolder = binding.historyPlaceHolder

        val recycler = binding.trackList
        val historyRecycler = binding.historyList

        val goHomeButton = binding.searchHomeButton
        val clearButton = binding.clearButton

        //Чтение истории
        sharedPreferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)

        if (userPreferences.readHistory(sharedPreferences).isNotEmpty()) {
            SearchHistory.setHistory(userPreferences.readHistory(sharedPreferences))
        }

        trackAdapter.tracks = trackList
        historyTrackAdapter.tracks = SearchHistory.getHistory()

        //кнопка назад
        goHomeButton.setOnClickListener {
            finish()
        }

        //кнопка отчистить
        clearButton.setOnClickListener {
            searchBar.setText("")
            clearList()

            placeholderImage.setImageDrawable(null)
            placeholderText.text = null
            placeholderButton.visibility = View.GONE

            historyPlaceHolder.visibility =
                if (SearchHistory.getHistory().isNotEmpty()) View.VISIBLE else View.GONE

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchBar.windowToken, 0)
        }

        //копка обновить
        placeholderButton.setOnClickListener {
            search()
        }

        //кнопка отчистить историю
        cleanHistoryButton.setOnClickListener {
            SearchHistory.clean()
            historyTrackAdapter.notifyDataSetChanged()
            historyPlaceHolder.visibility = View.GONE
        }

        //TODO Настройка SearchBar
        //Обработка логики, при наборе текста слежение за вводом пользователя, вотчер можно создать отдельно
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchBarHint.visibility =
                    if (searchBar.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE

                if (searchBar.hasFocus() && s?.isEmpty() == true && SearchHistory.getHistory()
                        .isNotEmpty()
                ) {
                    historyPlaceHolder.visibility = View.VISIBLE
                    clearList()
                } else {
                    historyPlaceHolder.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        //Обработка запроса
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        //Обработка вывода подсказки
        searchBar.setOnFocusChangeListener { view, hasFocus ->
            searchBarHint.visibility =
                if (hasFocus && searchBar.text.isEmpty()) View.VISIBLE else View.GONE

            historyPlaceHolder.visibility =
                if (hasFocus && searchBar.text.toString().isEmpty() && SearchHistory.getHistory()
                        .isNotEmpty()
                ) View.VISIBLE else View.GONE
        }

        //настраиваем ресайклер
        recycler.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = trackAdapter
        }

        historyRecycler.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = historyTrackAdapter
        }
    }

    //Записываем историю
    override fun onStop() {
        super.onStop()
        userPreferences.writeHistory(sharedPreferences, SearchHistory.getHistory())
    }

    //сохраняем данные
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("USER_INPUT", binding.searchBar.text.toString())
        lastSearchRequest = binding.searchBar.text.toString()
    }

    //вспоминаем данные
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastSearchRequest = savedInstanceState.getString("USER_INPUT").toString()
        binding.searchBar.setText(lastSearchRequest)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearList() {
        trackList.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun showPlaceHolder(text: String) {
        clearList()

        val placeholderImage = binding.placeholderImage
        val placeholderText = binding.placeholderText
        val button = binding.placeholderButton

        fun getPlaceholderResourceId(attributeId: Int): Int {
            val typedValue = TypedValue()
            theme.resolveAttribute(attributeId, typedValue, true)
            return typedValue.resourceId
        }

        when (text) {
            getString(R.string.something_went) -> {
                placeholderImage.setImageResource(getPlaceholderResourceId(R.attr.placeholder_image_error))
                placeholderText.setText(R.string.something_went)
                button.visibility = View.VISIBLE
            }

            getString(R.string.nothing_found) -> {
                placeholderImage.setImageResource(getPlaceholderResourceId(R.attr.placeholder_image_not_found))
                placeholderText.setText(R.string.nothing_found)
            }
        }
    }

    private fun search() {
        if (searchBar.text.isNotEmpty()) {
            historyPlaceHolder.visibility = View.GONE
            itunesService.search(searchBar.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.code() == 200) {
                            clearList()

                            placeholderImage.setImageDrawable(null)
                            placeholderText.text = null
                            placeholderButton.visibility = View.GONE

                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            } else {
                                showPlaceHolder(getString(R.string.nothing_found))
                            }
                        } else {
                            showPlaceHolder(getString(R.string.something_went))
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        showPlaceHolder(getString(R.string.something_went))
                    }
                })
        } else {
            clearList()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTrackToHistory(track: Track) {
        SearchHistory.add(track)
        historyTrackAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeTrackFromHistory(track: Track) {
        SearchHistory.remove(track)
        historyTrackAdapter.notifyDataSetChanged()
    }
}