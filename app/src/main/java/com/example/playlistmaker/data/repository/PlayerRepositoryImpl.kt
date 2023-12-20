package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.data.mapper.DataMapper
import com.example.playlistmaker.data.model.PlayerData
import com.example.playlistmaker.domain.models.CurrentTimeDomainModel
import com.example.playlistmaker.domain.models.PlayerStateDomain
import com.example.playlistmaker.domain.models.TrackDomainModel
import com.example.playlistmaker.domain.repository.PlayerRepository

class PlayerRepositoryImpl() : PlayerRepository {

    private lateinit var player: PlayerData
    private lateinit var playerState: PlayerStateDomain

    private var preparedCallBack: (() -> Unit)? = null
    private var completionCallBack: (() -> Unit)? = null



    override fun prepare(track: TrackDomainModel) {
        val currentTrack = DataMapper.mapToStorageTrackModel(
            track
        )


        player = PlayerData(MediaPlayer())

        player.player.setDataSource(currentTrack.previewUrl)
        player.player.prepareAsync()
        player.player.setOnPreparedListener {
            preparedCallBack?.invoke()

        }
        player.player.setOnCompletionListener {
            completionCallBack?.invoke()

//
//            playerState
//            resetTimer()
//            binding.playerDuration.setText(R.string.player_track_length)
//            setPlayButtonIcon(R.attr.play_button)
        }
    }

    override fun pause() {
        player.player.pause()
    }

    override fun play() {
        player.player.start()
    }

    override fun release() {
        player.player.release()
    }

    override fun getCurrentPosition(): CurrentTimeDomainModel {
        return CurrentTimeDomainModel(player.player.currentPosition)
    }

    override fun setOnPreparedListener(callback: ()-> Unit) {
        preparedCallBack = callback
    }

    override fun setOnCompletionListener(callback: ()-> Unit){
        completionCallBack = callback
    }

    override fun setCurrentState(playerStateDomain: PlayerStateDomain) {
        playerState = playerStateDomain
    }

    override fun getCurrentState(): PlayerStateDomain {
        return playerState
    }

    override fun isPlaying(): Boolean {
        return player.player.isPlaying
    }


}
