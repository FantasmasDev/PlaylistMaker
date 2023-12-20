package com.example.playlistmaker.Creator

import com.example.playlistmaker.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.usecase.PlayerEntity

object Creator {
    fun providePlayerEntity():PlayerEntity{
        return PlayerEntity(providePlayerRepository())
    }

    private fun providePlayerRepository(): PlayerRepository{
        return PlayerRepositoryImpl()
    }
}