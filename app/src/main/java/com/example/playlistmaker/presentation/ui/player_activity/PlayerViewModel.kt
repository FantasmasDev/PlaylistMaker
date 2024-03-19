package com.example.playlistmaker.presentation.ui.player_activity

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayerStateDomain
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.usecase.PlayerInteractor
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.ui.player_activity.models.PlayerViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val track: TrackDomain,
    context: Context
) : ViewModel() {

    companion object {
        private const val SECOND = 1000L
        private const val HALF_SECOND = 500L
        private const val THIRD_OF_SECOND = 300L
    }

    val handler = Handler(Looper.getMainLooper())
    private var timerJob: Job? = null

    private val defTime = context.getString(R.string.player_track_default_current_time)

    private var viewState: MutableLiveData<PlayerViewState> =
        MutableLiveData<PlayerViewState>().apply {
            value = PlayerViewState(false, defTime)
        }

    fun getViewState(): LiveData<PlayerViewState> = viewState

    fun preparePlayer() {
        playerInteractor.setOnPreparedListener(::preparedListener)
        playerInteractor.setOnCompletionListener(::completionListener)
        playerInteractor.prepare(track)
    }

    private fun preparedListener() {
        playerInteractor.setState(PlayerStateDomain.STATE_PREPARED)
    }

    private fun completionListener() {
        playerInteractor.setState(PlayerStateDomain.STATE_PREPARED)
        timerJob?.cancel()
        viewState.postValue(PlayerViewState(false, defTime))
    }

    private fun startPlayer() {
        playerInteractor.play()
        handler.postDelayed({ updateTimer() }, HALF_SECOND)
        viewState.postValue(viewState.value?.let { PlayerViewState(true, it.currentTime) })
        playerInteractor.setState(PlayerStateDomain.STATE_PLAYING)
    }

    fun pausePlayer() {
        timerJob?.cancel()
        playerInteractor.pause()
        viewState.postValue(viewState.value?.let { PlayerViewState(false, it.currentTime) })
        playerInteractor.setState(PlayerStateDomain.STATE_PAUSED)
    }

    fun playbackControl() {
        when (
            playerInteractor.getCurrentState()
        ) {
            PlayerStateDomain.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerStateDomain.STATE_PREPARED, PlayerStateDomain.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {
                return
            }
        }
    }

    private fun updateTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(THIRD_OF_SECOND)
                viewState.postValue(viewState.value?.let {
                    PlayerViewState(
                        it.isPlaying, SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(
                            TrackMapper.mapToCurrentTimePresentationModel(playerInteractor.getCurrentPosition()).time
                        )
                    )
                })
            }
        }
    }


    fun release() {
        playerInteractor.release()
    }
}