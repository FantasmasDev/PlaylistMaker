package com.example.playlistmaker.presentation.ui.player_activity

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayerStateDomain
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.usecase.PlayerEntity
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.ui.player_activity.models.PlayerViewState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerEntity: PlayerEntity,
    private val track: TrackDomain,
    context: Context
) : ViewModel() {

    companion object {
        private const val SECOND = 1000L
        private const val HALF_SECOND = 500L
        private const val THIRD_OF_SECOND = 333L
    }

    val handler = Handler(Looper.getMainLooper())

    private val defTime = context.getString(R.string.player_track_default_current_time)

    private var timerRunnable: Runnable? = null

    private var viewState: MutableLiveData<PlayerViewState> =
        MutableLiveData<PlayerViewState>().apply {
            value = PlayerViewState(false, defTime)
        }

    fun getViewState(): LiveData<PlayerViewState> = viewState

    fun preparePlayer() {
        playerEntity.setOnPreparedListener(::preparedListener)
        playerEntity.setOnCompletionListener(::completionListener)
        playerEntity.prepare(track)
    }

    private fun preparedListener() {
        playerEntity.setState(PlayerStateDomain.STATE_PREPARED)
    }

    private fun completionListener() {
        playerEntity.setState(PlayerStateDomain.STATE_PREPARED)
        resetTimer()
        viewState.postValue(PlayerViewState(false, defTime))
    }

    private fun startPlayer() {
        playerEntity.play()
        handler.postDelayed({ updateTimer() }, HALF_SECOND)
        viewState.postValue(viewState.value?.let { PlayerViewState(true, it.currentTime) })
        playerEntity.setState(PlayerStateDomain.STATE_PLAYING)
    }

    fun pausePlayer() {
        resetTimer()
        playerEntity.pause()
        viewState.postValue(viewState.value?.let { PlayerViewState(false, it.currentTime) })
        playerEntity.setState(PlayerStateDomain.STATE_PAUSED)
    }

    fun playbackControl() {
        when (
            playerEntity.getCurrentState()
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
        if (
            playerEntity.isPlaying()
            && playerEntity.getCurrentState() == PlayerStateDomain.STATE_PLAYING
        ) {
            timerRunnable = object : Runnable {
                override fun run() {
                    viewState.postValue(viewState.value?.let {
                        PlayerViewState(
                            it.isPlaying, SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(
                                TrackMapper.mapToCurrentTimePresentationModel(playerEntity.getCurrentPosition()).time
                            )
                        )
                    })
                    handler.postDelayed(this, THIRD_OF_SECOND)
                }
            }
            handler.post(timerRunnable!!)
        }
    }

    fun resetTimer() {
        if (timerRunnable != null && (playerEntity.getCurrentState() == PlayerStateDomain.STATE_PAUSED
                    ||
                    playerEntity.getCurrentState() == PlayerStateDomain.STATE_PREPARED
                    ) &&

            !playerEntity.isPlaying()
        ) {
            handler.removeCallbacksAndMessages(null)
            timerRunnable = null
        }
    }

    fun release() {
        playerEntity.release()
    }
}