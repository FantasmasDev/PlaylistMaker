package com.example.playlistmaker.presentation.ui.search_activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.Creator
import java.lang.ref.WeakReference

class SearchViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val tracksInteractor by lazy { Creator.provideTrackInteractor(context) }
    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val writeHistoryUseCase by lazy { Creator.getWriteHistoryUseCase(context) }
    private val readHistoryUseCase by lazy { Creator.getReadHistoryUseCase(context) }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val context = contextRef.get() ?: throw IllegalStateException("Context is null")
        return SearchViewModel(
            tracksInteractor = tracksInteractor,
            writeHistoryUseCase = writeHistoryUseCase,
            readHistoryUseCase = readHistoryUseCase,
            context = context
        ) as T
    }
}