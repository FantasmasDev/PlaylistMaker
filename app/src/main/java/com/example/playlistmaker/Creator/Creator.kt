package com.example.playlistmaker.Creator

import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.repository.ItunesTrackRepositoryImpl
import com.example.playlistmaker.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.domain.repository.ItunesTrackRepository
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.usecase.PlayerEntity
import com.example.playlistmaker.domain.usecase.TracksInteractor
import com.example.playlistmaker.domain.usecase.TracksInteractorImpl

object Creator {
    fun providePlayerEntity(): PlayerEntity {
        return PlayerEntity(providePlayerRepository())
    }

    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun getTrackRepository(): ItunesTrackRepository {
        return ItunesTrackRepositoryImpl(RetrofitClient)
    }

    fun provideTrackInteractor(): TracksInteractor{
        return TracksInteractorImpl(getTrackRepository())
    }

}