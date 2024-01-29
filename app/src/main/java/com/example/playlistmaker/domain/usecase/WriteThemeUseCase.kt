package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.ThemeStateParam
import com.example.playlistmaker.domain.repository.SharedRepository

class WriteThemeUseCase(private val sharedRepository: SharedRepository) {
    fun execute(param: ThemeStateParam) {
        sharedRepository.saveTheme(param)
    }
}