package com.example.playlistmaker.presentation.ui.library_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecase.ReadThemeUseCase
import com.example.playlistmaker.presentation.ui.library_activity.models.LibraryFragmentState

class LibraryFragmentViewModel(private val fragmentType: Int) : ViewModel() {

    private var libraryFragmentScreenState = MutableLiveData<LibraryFragmentState>()

    init {
        initState()
    }

    fun getFragmentState(): LiveData<LibraryFragmentState> = libraryFragmentScreenState

    fun initState() {
        val themeState = LibraryFragmentState(fragmentType = fragmentType)
        libraryFragmentScreenState.postValue(themeState)
    }
}