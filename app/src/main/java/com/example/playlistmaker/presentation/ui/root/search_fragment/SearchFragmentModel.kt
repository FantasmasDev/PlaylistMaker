package com.example.playlistmaker.presentation.ui.root.search_fragment

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.models.TrackHistory
import com.example.playlistmaker.domain.usecase.shared_cases.ReadHistoryUseCase
import com.example.playlistmaker.domain.usecase.TracksInteractor
import com.example.playlistmaker.domain.usecase.shared_cases.WriteHistoryUseCase
import com.example.playlistmaker.presentation.ui.root.search_fragment.models.ScreenInformation
import com.example.playlistmaker.presentation.ui.root.search_fragment.models.SearchFragmentState
import java.lang.ref.WeakReference

class SearchFragmentModel(
    private val tracksInteractor: TracksInteractor,
    private val writeHistoryUseCase: WriteHistoryUseCase,
    private val readHistoryUseCase: ReadHistoryUseCase,
    context: Context
) : ViewModel() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private var trackList = ArrayList<TrackDomain>()
    private var historyTrackList =
        readHistoryUseCase.execute().history.takeIf { it.isNotEmpty() } ?: ArrayList<TrackDomain>()

    private var viewState = MutableLiveData<ScreenInformation>().apply {
        postValue(
            ScreenInformation(
                screenState = SearchFragmentState.Content(
                    tracks = trackList
                ),
                history = historyTrackList
            )
        )
    }

    fun getViewState(): LiveData<ScreenInformation> = viewState


    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        private val SEARCH_REQUEST_TOKEN = Any()
    }

    fun searchDebounce(searchText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { search(searchText) }
        handler.postDelayed(searchRunnable, SEARCH_REQUEST_TOKEN, SEARCH_DEBOUNCE_DELAY)
    }

    fun setEmpty(){
        viewState.postValue(ScreenInformation(screenState = SearchFragmentState.Content(tracks = ArrayList<TrackDomain>()), history = historyTrackList))
    }

    fun handlerClear() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun clearTrackList() {
        trackList.clear()
    }

    fun search(searchText: String) {
        viewState.postValue(ScreenInformation(screenState = SearchFragmentState.Loading, history = historyTrackList))
        val context = contextRef.get()

        if (
            searchText.isNotEmpty()
        ) {
            tracksInteractor.searchTracks(
                searchText,
                object : TracksInteractor.TracksConsumer {
                    override fun consume(
                        foundTracks: ArrayList<TrackDomain>?,
                        errorMessage: String?
                    ) {
                        handler.post {

                            clearTrackList()

                            if (foundTracks != null) {
                                trackList.addAll(foundTracks.map {
                                    TrackDomain(
                                        trackName = it.trackName,
                                        artistName = it.artistName,
                                        trackTimeMillis = it.trackTimeMillis,
                                        artworkUrl100 = it.artworkUrl100,
                                        collectionName = it.collectionName,
                                        releaseDate = it.releaseDate,
                                        primaryGenreName = it.primaryGenreName,
                                        country = it.country,
                                        previewUrl = it.previewUrl
                                    )
                                } as ArrayList<TrackDomain>)
                            }

                            if (errorMessage != null) {
                                if (context != null) {
                                    viewState.postValue(ScreenInformation(screenState = SearchFragmentState.Error(errorMessage = context.getString(R.string.something_went)), history = historyTrackList))
                                }
                            } else if (trackList.isEmpty() == true) {
                                if (context != null) {
                                    viewState.postValue(ScreenInformation(screenState = SearchFragmentState.Empty(message = context.getString(R.string.nothing_found)), history = historyTrackList))
                                }
                            } else {
                                viewState.postValue(ScreenInformation(screenState = SearchFragmentState.Content(tracks = trackList), history = historyTrackList))
                            }
                        }
                    }
                })
        } else {
            clearTrackList()
        }
    }

    fun writeHistory() {
        writeHistoryUseCase.execute(TrackHistory(historyTrackList))
    }

    fun clearHistory() {
        historyTrackList.clear()
        viewState.value?.history = historyTrackList
    }

    fun addTrack(track: TrackDomain) {
        if (historyTrackList.size < 10) {
            historyTrackList.remove(track)
            historyTrackList.add(0, track)
        } else {
            historyTrackList.remove(track)
            historyTrackList.removeLast()
            historyTrackList.add(0, track)
        }
        viewState.value?.history = historyTrackList
    }
}