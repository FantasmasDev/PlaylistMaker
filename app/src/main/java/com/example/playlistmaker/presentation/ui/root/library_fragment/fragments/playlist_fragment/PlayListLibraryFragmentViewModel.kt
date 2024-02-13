package com.example.playlistmaker.presentation.ui.root.library_fragment.fragments.playlist_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.presentation.ui.root.library_fragment.fragments.models.PlayListLibraryFragmentState

class PlayListLibraryFragmentViewModel () : ViewModel() {

    private var libraryFragmentScreenState = MutableLiveData<PlayListLibraryFragmentState>()

}