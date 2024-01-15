package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.TrackDomain

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<TrackDomain>)
    }
}