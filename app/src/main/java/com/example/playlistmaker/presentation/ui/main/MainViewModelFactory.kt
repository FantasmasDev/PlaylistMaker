package com.example.playlistmaker.presentation.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.Creator
import java.lang.ref.WeakReference

class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val readThemeUseCase by lazy { Creator.getReadThemeUseCase(context) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            readThemeUseCase = readThemeUseCase
        ) as T
    }
}