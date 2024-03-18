package com.example.playlistmaker.presentation.ui.root.search_fragment.models

import com.example.playlistmaker.domain.models.TrackDomain

sealed interface SearchFragmentState {

    object Loading : SearchFragmentState

    data class Content(
        val tracks: ArrayList<TrackDomain>,
    ) : SearchFragmentState

    data class Error(
        val errorMessage: String
    ) : SearchFragmentState

    data class Empty(
        val message: String
    ) : SearchFragmentState

}
