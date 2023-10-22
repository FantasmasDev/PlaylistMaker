package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
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
    private val trackList = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(trackList)
    private lateinit var searchBar: EditText
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button

    //сохраняему данные
    private lateinit var lastSearchRequest: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //инициализация кнопок
        val recycler = binding.trackList
        val goHomeButton = binding.searchHomeButton
        val clearButton = binding.clearButton
        searchBar = binding.searchBar
        placeholderImage = binding.placeholderImage
        placeholderText = binding.placeholderText
        placeholderButton = binding.placeholderButton

        //кнопка назад
        goHomeButton.setOnClickListener {
            finish()
        }

        //кнопка отчистить
        clearButton.setOnClickListener {
            searchBar.setText("")
            clearList()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchBar.windowToken, 0)
        }

        //копка обновить
        placeholderButton.setOnClickListener {
            search()
        }

        //слежение за вводом пользователя
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        searchBar.addTextChangedListener(simpleTextWatcher)

        //Обработка запроса
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                search()
                true
            }
            false
        }

        //настраиваем ресайклер
        recycler.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = trackAdapter
        }
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
            getTheme().resolveAttribute(attributeId, typedValue, true)
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

    fun search() {
        if (searchBar.text.isNotEmpty()) {
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
                            placeholderText.setText(null)
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
}