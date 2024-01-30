package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.data.mapper.DataMapper
import com.example.playlistmaker.domain.models.CurrentTimeDomainModel
import com.example.playlistmaker.domain.models.PlayerStateDomain
import com.example.playlistmaker.domain.models.TrackURLDomainModel
import com.example.playlistmaker.domain.repository.PlayerRepository

class PlayerRepositoryImpl(private val player: MediaPlayer) : PlayerRepository {

    private var playerState: PlayerStateDomain = PlayerStateDomain.STATE_DEFAULT

    private var preparedCallBack: (() -> Unit)? = null
    private var completionCallBack: (() -> Unit)? = null

    override fun prepare(track: TrackURLDomainModel) {
        val currentTrack = DataMapper.mapToStorageTrackModel(
            track
        )

        player.setDataSource(currentTrack.previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            preparedCallBack?.invoke()

        }
        player.setOnCompletionListener {
            completionCallBack?.invoke()
        }
    }

    override fun pause() {
        player.pause()
    }

    override fun play() {
        player.start()
    }

    override fun release() {
        player.release()
    }

    override fun getCurrentPosition(): CurrentTimeDomainModel {
        return CurrentTimeDomainModel(player.currentPosition)
    }

    override fun setOnPreparedListener(callback: () -> Unit) {
        preparedCallBack = callback
    }

    override fun setOnCompletionListener(callback: () -> Unit) {
        completionCallBack = callback
    }

    override fun setCurrentState(playerStateDomain: PlayerStateDomain) {
        playerState = playerStateDomain
    }

    override fun getCurrentState(): PlayerStateDomain {
        return playerState
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying
    }
}
