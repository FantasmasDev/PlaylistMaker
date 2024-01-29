package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Resource<ArrayList<TrackDomain>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as TracksResponse).results.map {
                    TrackDomain(
                        trackName = it.trackName ?: "",
                        artistName = it.artistName ?: "",
                        trackTimeMillis = it.trackTimeMillis ?: 0,
                        artworkUrl100 = it.artworkUrl100 ?: "",
                        collectionName = it.collectionName ?: "",
                        releaseDate = it.releaseDate ?: "",
                        primaryGenreName = it.primaryGenreName ?: "",
                        country = it.country ?: "",
                        previewUrl = it.previewUrl ?: ""
                    )
                } as ArrayList)
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}