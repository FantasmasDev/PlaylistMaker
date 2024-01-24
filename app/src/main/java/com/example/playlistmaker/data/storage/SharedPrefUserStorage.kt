package com.example.playlistmaker.data.storage

import android.content.Context
import com.example.playlistmaker.data.dto.SavedHistoryOfTracks
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.models.NightMode
import com.google.gson.Gson

private const val SHARED_PREFS_NAME = "shared_prefs_name"
private const val THEME_NIGHT_MODE_STATE = "switch_night_mode_state"
private const val TRACKS_HISTORY = "tracks_history"

class SharedPrefUserStorage(context: Context) : UserStorage {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveTheme(isNightMode: NightMode) {
        sharedPreferences.edit().putBoolean(THEME_NIGHT_MODE_STATE, isNightMode.isEnable).apply()
    }

    override fun getTheme(): NightMode {
        val saved = sharedPreferences.getBoolean(THEME_NIGHT_MODE_STATE, false)
        val nightMode = NightMode(isEnable = saved)
        return nightMode
    }

    override fun saveHistory(saveTracks: SavedHistoryOfTracks) {
        val json = Gson().toJson(saveTracks.tracks)
        sharedPreferences.edit().putString(TRACKS_HISTORY, json).apply()
    }

    override fun getHistory(): SavedHistoryOfTracks {
        val json = sharedPreferences.getString(TRACKS_HISTORY, null) ?: return SavedHistoryOfTracks(
            ArrayList<TrackDto>()
        )
        val list = Gson().fromJson(json, Array<TrackDto>::class.java)
        return SavedHistoryOfTracks(tracks = list.toCollection(ArrayList<TrackDto>()))
    }
}