package com.example.playlistmaker.presentation.ui.search_activity.models

import com.example.playlistmaker.domain.models.TrackDomain

sealed interface SearchViewState {

    object Loading : SearchViewState

    data class Content(
        val tracks: ArrayList<TrackDomain>,
    ) : SearchViewState

    data class Error(
        val errorMessage: String
    ) : SearchViewState

    data class Empty(
        val message: String
    ) : SearchViewState

}
