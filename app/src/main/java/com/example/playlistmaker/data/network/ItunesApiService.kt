package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TracksResponse

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {
    @GET("/search")
    suspend fun searchTracks(@Query("term") term: String): TracksResponse
}
