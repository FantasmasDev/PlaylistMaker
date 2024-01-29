package com.example.playlistmaker.data.dto

class TracksResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()