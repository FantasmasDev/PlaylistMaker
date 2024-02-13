package com.example.playlistmaker.DI

import com.example.playlistmaker.domain.usecase.PlayerInteractor
import com.example.playlistmaker.domain.usecase.shared_cases.ReadHistoryUseCase
import com.example.playlistmaker.domain.usecase.shared_cases.ReadThemeUseCase
import com.example.playlistmaker.domain.usecase.TracksInteractor
import com.example.playlistmaker.domain.usecase.shared_cases.WriteHistoryUseCase
import com.example.playlistmaker.domain.usecase.shared_cases.WriteThemeUseCase
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