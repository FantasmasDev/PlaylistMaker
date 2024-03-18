package com.example.playlistmaker.domain.usecase.impl
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.usecase.TracksInteractor
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<ArrayList<TrackDomain>?, String?>> {

        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}