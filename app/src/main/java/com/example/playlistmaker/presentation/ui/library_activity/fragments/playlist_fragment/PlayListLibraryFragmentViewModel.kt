package com.example.playlistmaker.presentation.ui.library_activity.fragments.playlist_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.presentation.ui.library_activity.fragments.models.PlayListLibraryFragmentState

class PlayListLibraryFragmentViewModel () : ViewModel() {

    private var libraryFragmentScreenState = MutableLiveData<PlayListLibraryFragmentState>()

}