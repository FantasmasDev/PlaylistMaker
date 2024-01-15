package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.repository.ItunesTrackRepository
import com.example.playlistmaker.presentation.models.Track

class GetTrackListFromItunes(private val itunesTrackRepository: ItunesTrackRepository) {

    fun execute(): ArrayList<Track> {
        return itunesTrackRepository.getTrackList()
    }
}