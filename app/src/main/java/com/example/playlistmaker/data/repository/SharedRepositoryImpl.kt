package com.example.playlistmaker.data.repository


import com.example.playlistmaker.data.dto.SavedHistoryOfTracks
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.models.NightMode
import com.example.playlistmaker.data.storage.UserStorage
import com.example.playlistmaker.domain.models.ThemeStateParam
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.domain.models.TrackHistory
import com.example.playlistmaker.domain.repository.SharedRepository

class SharedRepositoryImpl(private val userStorage: UserStorage) : SharedRepository {
    override fun saveTheme(saveParam: ThemeStateParam) {
        val isNightMode = NightMode(isEnable = saveParam.themeState)
        userStorage.saveTheme(isNightMode)
    }

    override fun getTheme(): ThemeStateParam {
        val isNightMode: NightMode = userStorage.getTheme()
        val savedState = ThemeStateParam(themeState = isNightMode.isEnable)
        return savedState
    }

    override fun saveHistory(saveParam: TrackHistory) {
        val history = SavedHistoryOfTracks(tracks = saveParam.history.map {
            TrackDto(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl
            )
        } as ArrayList<TrackDto>)
        userStorage.saveHistory(history)
    }

    override fun getHistory(): TrackHistory {
        val savedHistory: SavedHistoryOfTracks = userStorage.getHistory()
        val trackHistory = TrackHistory(history = savedHistory.tracks.map {
            TrackDomain(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl
            )
        } as ArrayList<TrackDomain>)
        return trackHistory
    }
}