package com.example.playlistmaker.domain.usecase.impl

import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.usecase.TracksInteractor
import com.example.playlistmaker.util.Resource

class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
        t.start()
    }
}