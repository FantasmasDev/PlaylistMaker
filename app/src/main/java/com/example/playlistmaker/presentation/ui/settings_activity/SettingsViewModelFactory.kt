package com.example.playlistmaker.presentation.ui.settings_activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.Creator

class SettingsViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val writeThemeUseCase by lazy { Creator.getWriteThemeUseCase(context) }
    private val readThemeUseCase by lazy { Creator.getReadThemeUseCase(context) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            writeThemeUseCase = writeThemeUseCase,
            readThemeUseCase = readThemeUseCase,
        ) as T
    }

}