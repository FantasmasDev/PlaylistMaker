package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
    private  val baseURL = "https://itunes.apple.com"

    //Создаём ретрофит, создаём OkHTTP и перехватчик
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
            )
        .build()

    private val itunesService = retrofit.create(itunesAPI::class.java)

    private lateinit var binding: ActivitySearchBinding

    private val trackList = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(trackList)

    //сохраняему данные
    private lateinit var lastSearchRequest: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //инициализация кнопок
        val recycler = binding.trackList
        val goHomeButton = binding.searchHomeButton
        val clearButton = binding.clearButton
        val searchBar = binding.searchBar


        //кнопка назад
        goHomeButton.setOnClickListener {
            finish()
        }

        //кнопка отчистить
        clearButton.setOnClickListener {
            searchBar.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchBar.windowToken, 0)
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

        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                if (searchBar.text.isNotEmpty()) {
                    itunesService.search(searchBar.text.toString())
                        .enqueue(object : Callback<TracksResponse> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(
                                call: Call<TracksResponse>,
                                response: Response<TracksResponse>
                            ) {
                                if (response.code() == 200) {
                                    trackList.clear()
                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        trackList.addAll(response.body()?.results!!)
                                        trackAdapter.notifyDataSetChanged()
                                    }
                                    if (trackList.isEmpty()) {
                                        showMessage(getString(R.string.nothing_found), "")
                                    } else {
                                        showMessage("", "")
                                    }
                                } else {
                                    showMessage(
                                        getString(R.string.something_went_wrong),
                                        response.code().toString()
                                    )
                                }
                            }

                            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                                showMessage(
                                    getString(R.string.something_went_wrong),
                                    t.message.toString()
                                )
                            }
                        })
                }

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
    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            //placeholderMessage.visibility = View.VISIBLE
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            //placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            //placeholderMessage.visibility = View.GONE
        }
    }
}