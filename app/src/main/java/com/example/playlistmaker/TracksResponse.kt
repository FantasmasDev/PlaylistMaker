package com.example.playlistmaker

import com.example.playlistmaker.presentation.model.Track

class TracksResponse(
    val resultCount: Int,
    val results: List<Track>
)