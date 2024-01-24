package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SharedRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefUserStorage
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.repository.SharedRepository
import com.example.playlistmaker.domain.usecase.PlayerEntity
import com.example.playlistmaker.domain.usecase.ReadHistoryUseCase
import com.example.playlistmaker.domain.usecase.ReadThemeUseCase
import com.example.playlistmaker.domain.usecase.TracksInteractor
import com.example.playlistmaker.domain.usecase.WriteHistoryUseCase
import com.example.playlistmaker.domain.usecase.WriteThemeUseCase
import com.example.playlistmaker.domain.usecase.impl.TracksInteractorImpl

object Creator {
    fun providePlayerEntity(): PlayerEntity {
        return PlayerEntity(providePlayerRepository())
    }

    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TracksInteractor{
        return TracksInteractorImpl(getTrackRepository(context))
    }

    private fun getSharedRepository(context: Context): SharedRepository {
        return SharedRepositoryImpl(userStorage = SharedPrefUserStorage(context))
    }

    fun getReadHistoryUseCase(context: Context): ReadHistoryUseCase {
        return ReadHistoryUseCase(getSharedRepository(context))
    }

    fun getReadThemeUseCase(context: Context): ReadThemeUseCase{
        return  ReadThemeUseCase(getSharedRepository(context))
    }

    fun getWriteHistoryUseCase(context: Context): WriteHistoryUseCase{
        return WriteHistoryUseCase(getSharedRepository(context))
    }

    fun getWriteThemeUseCase(context: Context): WriteThemeUseCase{
        return WriteThemeUseCase(getSharedRepository(context))
    }
}