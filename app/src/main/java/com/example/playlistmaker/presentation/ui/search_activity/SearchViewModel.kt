package com.example.playlistmaker.presentation.ui.search_activity

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.models.TrackHistory
import com.example.playlistmaker.domain.usecase.ReadHistoryUseCase
import com.example.playlistmaker.domain.usecase.TracksInteractor
import com.example.playlistmaker.domain.usecase.WriteHistoryUseCase
import com.example.playlistmaker.presentation.ui.search_activity.models.ScreenInformation
import com.example.playlistmaker.presentation.ui.search_activity.models.SearchViewState
import java.lang.ref.WeakReference

class SearchViewModel(
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
                screenState = SearchViewState.Content(
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

    fun handlerClear() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun clearTrackList() {
        trackList.clear()
    }

    fun search(searchText: String) {
        viewState.postValue(ScreenInformation(screenState = SearchViewState.Loading, history = historyTrackList))
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
                                    viewState.postValue(ScreenInformation(screenState = SearchViewState.Error(errorMessage = context.getString(R.string.something_went)), history = historyTrackList))
                                }
                            } else if (trackList.isEmpty() == true) {
                                if (context != null) {
                                    viewState.postValue(ScreenInformation(screenState = SearchViewState.Empty(message = context.getString(R.string.nothing_found)), history = historyTrackList))
                                }
                            } else {
                                viewState.postValue(ScreenInformation(screenState = SearchViewState.Content(tracks = trackList), history = historyTrackList))
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