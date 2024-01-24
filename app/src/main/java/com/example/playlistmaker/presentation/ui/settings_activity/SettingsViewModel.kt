package com.example.playlistmaker.presentation.ui.settings_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.ThemeStateParam
import com.example.playlistmaker.domain.usecase.ReadThemeUseCase
import com.example.playlistmaker.domain.usecase.WriteThemeUseCase
import com.example.playlistmaker.presentation.ui.settings_activity.models.SettingsScreenState

class SettingsViewModel(
    private val writeThemeUseCase: WriteThemeUseCase,
    private val readThemeUseCase: ReadThemeUseCase
) : ViewModel() {

    private var settingsViewScreenState = MutableLiveData<SettingsScreenState>()
    init {
        readTheme()
    }


    fun getViewState(): LiveData<SettingsScreenState> = settingsViewScreenState

    fun writeTheme(theme: ThemeStateParam) {
        writeThemeUseCase.execute(theme)
    }

    fun readTheme() {
        val state = readThemeUseCase.execute()
        val themeState = SettingsScreenState(currentThemeState = state)
        settingsViewScreenState.value = themeState
    }
}