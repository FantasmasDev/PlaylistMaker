package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.ThemeStateParam
import com.example.playlistmaker.domain.repository.SharedRepository

class ReadThemeUseCase(private val sharedRepository: SharedRepository) {
    fun execute(): ThemeStateParam {
        return sharedRepository.getTheme()
    }
}