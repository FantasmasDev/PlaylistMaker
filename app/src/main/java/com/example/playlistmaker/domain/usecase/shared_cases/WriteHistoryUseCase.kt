package com.example.playlistmaker.domain.usecase.shared_cases

import com.example.playlistmaker.domain.models.TrackHistory
import com.example.playlistmaker.domain.repository.SharedRepository

class WriteHistoryUseCase(private val sharedRepository: SharedRepository) {
    fun execute(param: TrackHistory) {
        sharedRepository.saveHistory(param)
    }
}