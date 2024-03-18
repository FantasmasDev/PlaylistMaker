package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.TrackDomain
import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<ArrayList<TrackDomain>?, String?>>
}