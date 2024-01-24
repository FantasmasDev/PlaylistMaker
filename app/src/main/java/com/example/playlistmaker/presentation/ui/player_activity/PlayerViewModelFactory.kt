package com.example.playlistmaker.presentation.ui.player_activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.util.Creator
import java.lang.ref.WeakReference

class PlayerViewModelFactory(private val track: TrackDomain, context: Context) :
    ViewModelProvider.Factory {
    private val playerEntity by lazy { Creator.providePlayerEntity() }
    private val contextRef: WeakReference<Context> = WeakReference(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val context = contextRef.get() ?: throw IllegalStateException("Context is null")
        return PlayerViewModel(playerEntity = playerEntity, track = track, context = context) as T
    }
}