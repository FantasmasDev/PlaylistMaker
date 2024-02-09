package com.example.playlistmaker.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecase.shared_cases.ReadThemeUseCase
import com.example.playlistmaker.presentation.ui.main.models.MainScreenState

class MainViewModel(
    private val readThemeUseCase: ReadThemeUseCase
) : ViewModel() {

    private var mainViewScreenState = MutableLiveData<MainScreenState>()
    init {
        readTheme()
    }
    fun getViewState(): LiveData<MainScreenState> = mainViewScreenState

    fun readTheme() {
        val state = readThemeUseCase.execute()
        val themeState = MainScreenState(currentThemeState = state)
        mainViewScreenState.value = themeState
    }
}
