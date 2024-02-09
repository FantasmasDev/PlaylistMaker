package com.example.playlistmaker.presentation.ui.library_activity.fragments.library_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.presentation.ui.library_activity.fragments.models.LibraryFragmentState

class LibraryFragmentViewModel() : ViewModel() {

    private var libraryFragmentScreenState = MutableLiveData<LibraryFragmentState>()
}