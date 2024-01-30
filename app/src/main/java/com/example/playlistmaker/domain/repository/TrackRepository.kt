package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTracks(expression: String): Resource<ArrayList<TrackDomain>>
}