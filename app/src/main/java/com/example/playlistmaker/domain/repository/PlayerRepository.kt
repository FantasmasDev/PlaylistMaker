package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.CurrentTimeDomainModel
import com.example.playlistmaker.domain.models.PlayerStateDomain
import com.example.playlistmaker.domain.models.TrackDomainModel

interface PlayerRepository {
    fun prepare(track: TrackDomainModel)
    fun pause()
    fun play()

    fun release()

    fun getCurrentPosition(): CurrentTimeDomainModel

    fun setOnPreparedListener(callback: ()-> Unit)

    fun setOnCompletionListener(callback: ()-> Unit)

    fun setCurrentState(playerStateDomain: PlayerStateDomain)

    fun getCurrentState(): PlayerStateDomain

    fun isPlaying(): Boolean

}