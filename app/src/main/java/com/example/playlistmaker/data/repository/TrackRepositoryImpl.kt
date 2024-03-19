package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<ArrayList<TrackDomain>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as TracksResponse) {
                    val data = results.map{
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
                    } as ArrayList
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}