package com.example.playlistmaker.presentation.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.ThemeStateParam
import com.example.playlistmaker.domain.usecase.ReadThemeUseCase
import com.example.playlistmaker.domain.usecase.WriteThemeUseCase
import com.example.playlistmaker.presentation.ui.main.models.MainScreenState
import com.example.playlistmaker.presentation.ui.settings_activity.models.SettingsScreenState

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
