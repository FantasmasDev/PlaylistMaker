package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.repository.ItunesTrackRepository

class ItunesTrackRepositoryImpl(private val networkClient: NetworkClient) : ItunesTrackRepository {

    override fun searchTracks(expression: String): List<TrackDomain> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if(response.resultCode == 200){
            return (response as TracksResponse).results.map {
                TrackDomain(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
            }
        } else {
            return emptyList()
        }

    }
}