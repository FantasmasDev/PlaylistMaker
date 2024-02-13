package com.example.playlistmaker.DI

import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.presentation.ui.library_activity.fragments.library_fragment.LibraryFragmentViewModel
import com.example.playlistmaker.presentation.ui.library_activity.LibraryViewModel
import com.example.playlistmaker.presentation.ui.library_activity.fragments.playlist_fragment.PlayListLibraryFragmentViewModel
import com.example.playlistmaker.presentation.ui.main.MainViewModel
import com.example.playlistmaker.presentation.ui.player_activity.PlayerViewModel
import com.example.playlistmaker.presentation.ui.search_activity.SearchViewModel
import com.example.playlistmaker.presentation.ui.settings_activity.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(readThemeUseCase = get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(readThemeUseCase = get(), writeThemeUseCase = get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            tracksInteractor = get(),
            writeHistoryUseCase = get(),
            readHistoryUseCase = get(),
            context = get()
        )
    }

    viewModel<PlayerViewModel>() { (track: TrackDomain) ->
        PlayerViewModel(playerInteractor = get(), track = track, context = get())
    }

    viewModel<LibraryViewModel>() {
        LibraryViewModel()
    }

    viewModel<LibraryFragmentViewModel>() {
        LibraryFragmentViewModel()
    }

    viewModel<PlayListLibraryFragmentViewModel>() {
        PlayListLibraryFragmentViewModel()
    }
}