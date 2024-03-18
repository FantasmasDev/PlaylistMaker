package com.example.playlistmaker.DI

import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.presentation.ui.root.library_fragment.fragments.library_fragment.LibraryFragmentViewModel
import com.example.playlistmaker.presentation.ui.root.library_fragment.LibraryRootFragmentModel
import com.example.playlistmaker.presentation.ui.root.library_fragment.fragments.playlist_fragment.PlayListLibraryFragmentViewModel
import com.example.playlistmaker.presentation.ui.root.RootViewModel
import com.example.playlistmaker.presentation.ui.player_activity.PlayerViewModel
import com.example.playlistmaker.presentation.ui.root.search_fragment.SearchFragmentModel
import com.example.playlistmaker.presentation.ui.root.settings_fragment.SettingsFragmentModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<RootViewModel> {
        RootViewModel(readThemeUseCase = get())
    }

    viewModel<SettingsFragmentModel> {
        SettingsFragmentModel(readThemeUseCase = get(), writeThemeUseCase = get())
    }

    viewModel<SearchFragmentModel> {
        SearchFragmentModel(
            tracksInteractor = get(),
            writeHistoryUseCase = get(),
            readHistoryUseCase = get(),
            context = get()
        )
    }

    viewModel<PlayerViewModel>() { (track: TrackDomain) ->
        PlayerViewModel(playerInteractor = get(), track = track, context = get())
    }

    viewModel<LibraryRootFragmentModel>() {
        LibraryRootFragmentModel()
    }

    viewModel<LibraryFragmentViewModel>() {
        LibraryFragmentViewModel()
    }

    viewModel<PlayListLibraryFragmentViewModel>() {
        PlayListLibraryFragmentViewModel()
    }
}