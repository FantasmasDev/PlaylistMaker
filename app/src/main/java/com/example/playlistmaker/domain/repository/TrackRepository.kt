package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<ArrayList<TrackDomain>>>
}
