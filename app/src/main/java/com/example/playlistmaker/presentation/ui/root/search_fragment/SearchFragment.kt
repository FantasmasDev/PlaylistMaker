package com.example.playlistmaker.presentation.ui.root.search_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.ui.root.search_fragment.adapters.TrackAdapter
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.presentation.ui.player_activity.PlayerActivity
import com.example.playlistmaker.presentation.ui.root.search_fragment.models.SearchFragmentState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val USER_INPUT = "user_input"
    }

    private val vm by viewModel<SearchFragmentModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true
    private var userInput =""


    private val trackAdapter = TrackAdapter {
        openPlayer(it)
    }

    private val historyTrackAdapter = TrackAdapter {
        openPlayer(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = binding.trackList
        val historyRecycler = binding.historyList

        vm.getViewState().observe(viewLifecycleOwner) {
            when (it.screenState) {
                is SearchFragmentState.Loading -> {
                    binding.apply {
                        progressBar.isVisible = true
                        errorPlaceholder.isVisible = false
                        historyPlaceHolder.isVisible = false
                    }
                }

                is SearchFragmentState.Error -> {
                    binding.apply {
                        progressBar.isVisible = false
                        errorPlaceholder.isVisible = true
                        placeholderText.text =
                            (it.screenState as SearchFragmentState.Error).errorMessage
                        placeholderButton.isVisible= true
                        historyPlaceHolder.isVisible = false
                    }

                    setPlaceHolderImage((it.screenState as SearchFragmentState.Error).errorMessage)
                    trackAdapter.tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                }

                is SearchFragmentState.Empty -> {
                    binding.apply {
                        progressBar.isVisible = false
                        errorPlaceholder.isVisible = true
                        placeholderText.text = (it.screenState as SearchFragmentState.Empty).message
                        placeholderButton.isVisible = false
                        historyPlaceHolder.isVisible = false
                    }

                    setPlaceHolderImage((it.screenState as SearchFragmentState.Empty).message)
                    trackAdapter.tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                }

                is SearchFragmentState.Content -> {
                    binding.apply {
                        progressBar.isVisible = false
                        errorPlaceholder.isVisible = false
                        historyPlaceHolder.isVisible = false
                    }

                    recycler.apply {
                        layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        trackAdapter.tracks = (it.screenState as SearchFragmentState.Content).tracks
                        adapter = trackAdapter
                    }
                    trackAdapter.notifyDataSetChanged()
                }
            }
            historyTrackAdapter.tracks = it.history
        }


        binding.apply {

            clearButton.setOnClickListener {
                searchBar.setText("")
                clearList()
                errorPlaceholder.visibility = View.GONE

                historyPlaceHolder.visibility =
                    if (historyTrackAdapter.tracks.isNotEmpty()) View.VISIBLE else View.GONE

                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
            }

            placeholderButton.setOnClickListener {
                Log.e("butt", "pressed")
                vm.searchDebounce(binding.searchBar.text.toString())
            }

            cleanHistoryButton.setOnClickListener {
                vm.clearHistory()
                historyTrackAdapter.notifyDataSetChanged()
                historyPlaceHolder.visibility = View.GONE
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
                    clearButton.visibility = clearButtonVisibility(s)

                    userInput = if(s.isNullOrEmpty()) "" else searchBar.text.toString()

                    if (searchBar.hasFocus() && s?.isEmpty() == true && historyTrackAdapter.tracks.isNotEmpty()
                    ) {
                        historyPlaceHolder.isVisible = true
                        progressBar.isVisible = false
                        errorPlaceholder.isVisible = false
                        recycler.isVisible = false
                    } else {
                        historyPlaceHolder.isVisible = false
                        recycler.isVisible = true


                    }
                    if (!s.isNullOrEmpty()) {
                        historyPlaceHolder.isVisible = false

                        vm.searchDebounce(searchBar.text.toString())
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            searchBar.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    vm.search(searchBar.text.toString())
                    true
                }
                false
            }
            searchBar.setOnFocusChangeListener { _, hasFocus ->
                historyPlaceHolder.visibility =
                    if (hasFocus && searchBar.text.toString()
                            .isEmpty() && historyTrackAdapter.tracks
                            .isNotEmpty()
                    ) View.VISIBLE else View.GONE
            }
        }

        historyRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = historyTrackAdapter
        }
    }

    override fun onStop() {
        super.onStop()
        vm.writeHistory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
            outState.putString(USER_INPUT, userInput)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            val lastSearchRequest = savedInstanceState.getString(USER_INPUT).toString()
            binding.searchBar.setText(lastSearchRequest)
        }
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
            requireContext().theme.resolveAttribute(attributeId, typedValue, true)
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
            val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
            playerIntent.putExtra("track", track)
            startActivity(playerIntent)
            historyTrackAdapter.notifyDataSetChanged()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}