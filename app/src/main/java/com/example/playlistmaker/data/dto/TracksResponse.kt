package com.example.playlistmaker.data.dto

import com.example.playlistmaker.presentation.models.Track

class TracksResponse(
    val resultCount: Int,
    val results: List<TrackDTO>
) : Response()