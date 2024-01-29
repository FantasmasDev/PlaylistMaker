package com.example.playlistmaker.data.storage

import com.example.playlistmaker.data.dto.SavedHistoryOfTracks
import com.example.playlistmaker.data.models.NightMode

interface UserStorage {
    fun saveTheme(isNightMode: NightMode)
    fun getTheme(): NightMode

    fun saveHistory(saveTracks: SavedHistoryOfTracks)
    fun getHistory(): SavedHistoryOfTracks
}