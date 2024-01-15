package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.presentation.models.Track

interface ItunesTrackRepository {
    fun searchTracks(expression: String): List<TrackDomain>
}