package com.example.playlistmaker.DI

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.network.ItunesApiService
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SharedRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefUserStorage
import com.example.playlistmaker.data.storage.UserStorage
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.repository.SharedRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SHARED_PREFS_NAME = "shared_prefs_name"

val dataModule = module {
    factory<PlayerRepository> {
        PlayerRepositoryImpl()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(retrofit = get(), context = get())
    }

    single<ItunesApiService>{
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                    .build()
            )
            .build()
            .create(ItunesApiService::class.java)
    }



    single<TrackRepository> {
        TrackRepositoryImpl(networkClient = get())
    }

    single<UserStorage> {
        SharedPrefUserStorage(sharedPreferences = get(), context = get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    single<SharedRepository> {
        SharedRepositoryImpl(userStorage = get())
    }
}
