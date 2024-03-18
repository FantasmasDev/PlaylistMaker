package com.example.playlistmaker.presentation.ui.root.library_fragment.fragments.library_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.presentation.ui.root.library_fragment.fragments.models.LibraryFragmentState

class LibraryFragmentViewModel() : ViewModel() {

    private var libraryFragmentScreenState = MutableLiveData<LibraryFragmentState>()
}