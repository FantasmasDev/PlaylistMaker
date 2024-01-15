package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.repository.ItunesTrackRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: ItunesTrackRepository) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}