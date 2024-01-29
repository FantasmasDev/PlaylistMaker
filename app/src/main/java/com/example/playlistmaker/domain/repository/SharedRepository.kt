package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.ThemeStateParam
import com.example.playlistmaker.domain.models.TrackHistory

interface SharedRepository {
    fun saveTheme(saveParam: ThemeStateParam)
    fun getTheme(): ThemeStateParam
    fun saveHistory(saveParam: TrackHistory)
    fun getHistory(): TrackHistory
}