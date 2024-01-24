package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.TrackHistory
import com.example.playlistmaker.domain.repository.SharedRepository

class ReadHistoryUseCase(private val sharedRepository: SharedRepository) {
    fun execute(): TrackHistory {
        return sharedRepository.getHistory()
    }
}