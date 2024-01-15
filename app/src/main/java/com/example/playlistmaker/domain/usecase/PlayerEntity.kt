package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.CurrentTimeDomainModel
import com.example.playlistmaker.domain.models.PlayerStateDomain
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.models.Track

class PlayerEntity(private val playerRepository: PlayerRepository) {

    fun prepare(track: Track){
        playerRepository.prepare(TrackMapper.mapToDomain(track))
    }

    fun pause(){
        playerRepository.pause()
    }

    fun play(){
        playerRepository.play()
    }

    fun release(){
        playerRepository.release()
    }

    fun getCurrentPosition(): CurrentTimeDomainModel{
        return playerRepository.getCurrentPosition()
    }

    fun setOnPreparedListener(callback: ()-> Unit){
        playerRepository.setOnPreparedListener(callback)
    }

    fun setOnCompletionListener(callback: ()-> Unit){
        playerRepository.setOnCompletionListener(callback)
    }

    fun setState(playerStateDomain: PlayerStateDomain){
        playerRepository.setCurrentState(playerStateDomain)
    }

    fun getCurrentState(): PlayerStateDomain{
        return playerRepository.getCurrentState()
    }

    fun isPlaying():Boolean{
        return playerRepository.isPlaying()
    }



}