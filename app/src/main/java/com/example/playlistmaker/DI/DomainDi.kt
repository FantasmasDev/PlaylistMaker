package com.example.playlistmaker.DI

import android.content.Context
import com.example.playlistmaker.domain.usecase.PlayerInteractor
import com.example.playlistmaker.domain.usecase.ReadHistoryUseCase
import com.example.playlistmaker.domain.usecase.ReadThemeUseCase
import com.example.playlistmaker.domain.usecase.TracksInteractor
import com.example.playlistmaker.domain.usecase.WriteHistoryUseCase
import com.example.playlistmaker.domain.usecase.WriteThemeUseCase
import com.example.playlistmaker.domain.usecase.impl.TracksInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    factory<PlayerInteractor> {
        PlayerInteractor(playerRepository = get())
    }

    factory<TracksInteractor>{
        TracksInteractorImpl(repository = get())
    }

    factory<ReadHistoryUseCase> {
        ReadHistoryUseCase(sharedRepository = get())
    }

    factory<ReadThemeUseCase> {
        ReadThemeUseCase(sharedRepository = get())
    }

    factory<WriteHistoryUseCase> {
        WriteHistoryUseCase(sharedRepository = get())
    }

    factory<WriteThemeUseCase>{
        WriteThemeUseCase(sharedRepository = get())
    }

}