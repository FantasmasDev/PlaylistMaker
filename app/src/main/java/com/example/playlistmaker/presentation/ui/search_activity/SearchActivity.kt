package com.example.playlistmaker.presentation.ui.search_activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.ui.search_activity.adapters.TrackAdapter
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.presentation.ui.main.MainViewModel
import com.example.playlistmaker.presentation.ui.player_activity.PlayerActivity
import com.example.playlistmaker.presentation.ui.search_activity.models.SearchViewState
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val vm by viewModel<SearchViewModel>()
    private lateinit var binding: ActivitySearchBinding
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val trackAdapter = TrackAdapter {
        openPlayer(it)
    }

    private val historyTrackAdapter = TrackAdapter {
        openPlayer(it)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.trackList
        val historyRecycler = binding.historyList

        vm.getViewState().observe(this) {
            when (it.screenState) {
                is SearchViewState.Loading -> {
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        errorPlaceholder.visibility = View.GONE
                    }
                }

                is SearchViewState.Error -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        errorPlaceholder.visibility = View.VISIBLE
                        placeholderText.text =
                            (it.screenState as SearchViewState.Error).errorMessage
                        placeholderButton.visibility = View.VISIBLE
                    }

                    setPlaceHolderImage((it.screenState as SearchViewState.Error).errorMessage)
                    trackAdapter.tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                }

                is SearchViewState.Empty -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        errorPlaceholder.visibility = View.VISIBLE
                        placeholderText.text = (it.screenState as SearchViewState.Empty).message
                        placeholderButton.visibility = View.GONE
                    }

                    setPlaceHolderImage((it.screenState as SearchViewState.Empty).message)
                    trackAdapter.tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                }

                is SearchViewState.Content -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        errorPlaceholder.visibility = View.GONE
                    }

                    recycler.apply {
                        layoutManager =
                            LinearLayoutManager(
                                this@SearchActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        trackAdapter.tracks = (it.screenState as SearchViewState.Content).tracks
                        adapter = trackAdapter
                    }
                    trackAdapter.notifyDataSetChanged()
                }
            }
            historyTrackAdapter.tracks = it.history
        }


        binding.apply {
            goHomeButton.setOnClickListener {
                finish()
            }

            clearButton.setOnClickListener {
                binding.searchBar.setText("")
                clearList()
                binding.errorPlaceholder.visibility = View.GONE

                binding.historyPlaceHolder.visibility =
                    if (historyTrackAdapter.tracks.isNotEmpty()) View.VISIBLE else View.GONE

                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
            }

            placeholderButton.setOnClickListener {
                vm.search(binding.searchBar.text.toString())
            }

            cleanHistoryButton.setOnClickListener {
                vm.clearHistory()
                historyTrackAdapter.notifyDataSetChanged()
                binding.historyPlaceHolder.visibility = View.GONE
            }

            searchBar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.clearButton.visibility = clearButtonVisibility(s)

                    if (binding.searchBar.hasFocus() && s?.isEmpty() == true && historyTrackAdapter.tracks.isNotEmpty()
                    ) {
                        binding.historyPlaceHolder.visibility = View.VISIBLE
                        vm.setEmpty()
                    } else {
                        binding.historyPlaceHolder.visibility = View.GONE
                    }
                    if (!searchBar.text.equals("")) {
                        vm.searchDebounce(binding.searchBar.text.toString())
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            searchBar.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    vm.search(binding.searchBar.text.toString())
                    true
                }
                false
            }
            searchBar.setOnFocusChangeListener { view, hasFocus ->
                binding.historyPlaceHolder.visibility =
                    if (hasFocus && binding.searchBar.text.toString()
                            .isEmpty() && historyTrackAdapter.tracks
                            .isNotEmpty()
                    ) View.VISIBLE else View.GONE
            }
        }

        historyRecycler.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = historyTrackAdapter
        }
    }

    override fun onStop() {
        super.onStop()
        vm.writeHistory()
    }

    override fun onDestroy() {
        vm.handlerClear()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("USER_INPUT", binding.searchBar.text.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRestart() {
        super.onRestart()
        historyTrackAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val lastSearchRequest = savedInstanceState.getString("USER_INPUT").toString()
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
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun setPlaceHolderImage(text: String) {
        val placeholderImage = binding.placeholderImage
        fun getPlaceholderResourceId(attributeId: Int): Int {
            val typedValue = TypedValue()
            theme.resolveAttribute(attributeId, typedValue, true)
            return typedValue.resourceId
        }

        when (text) {
            getString(R.string.something_went) -> {
                placeholderImage.setImageResource(getPlaceholderResourceId(R.attr.placeholder_image_error))
            }

            getString(R.string.nothing_found) -> {
                placeholderImage.setImageResource(getPlaceholderResourceId(R.attr.placeholder_image_not_found))
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openPlayer(track: TrackDomain) {
        if (clickDebounce()) {
            vm.addTrack(track)
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra("track", track)
            startActivity(playerIntent)
            historyTrackAdapter.notifyDataSetChanged()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}