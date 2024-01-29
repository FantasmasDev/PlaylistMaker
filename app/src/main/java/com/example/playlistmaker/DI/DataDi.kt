package com.example.playlistmaker.DI

import android.content.Context
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SharedRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefUserStorage
import com.example.playlistmaker.data.storage.UserStorage
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.repository.SharedRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import org.koin.dsl.module

val dataModule = module {
    factory<PlayerRepository>{
        PlayerRepositoryImpl()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(context = get())
    }

    single<TrackRepository>{
        TrackRepositoryImpl(networkClient = get())
    }

    single<UserStorage>{
        SharedPrefUserStorage(context = get())
    }

    single<SharedRepository>{
        SharedRepositoryImpl(userStorage = get())
    }

}