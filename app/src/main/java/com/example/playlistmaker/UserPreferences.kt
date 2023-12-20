package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.presentation.model.Track
import com.example.playlistmaker.ui.main.THEME_SWITCH_STATE
import com.example.playlistmaker.ui.main.TRACKS_HISTORY
import com.google.gson.Gson

class UserPreferences {

    fun readHistory(sharedPreferences: SharedPreferences): ArrayList<Track> {
        val json = sharedPreferences.getString(TRACKS_HISTORY, null) ?: return ArrayList<Track>()
        val list = Gson().fromJson(json, Array<Track>::class.java)
        return list.toCollection(ArrayList<Track>())
    }

    fun readSwitchState(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(THEME_SWITCH_STATE, false)
    }

    fun writeHistory(sharedPreferences: SharedPreferences, trackList: ArrayList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY, json)
            .apply()
    }

    fun writeTheme(sharedPreferences: SharedPreferences, switchState: Boolean) {
        sharedPreferences.edit()
            .putBoolean(THEME_SWITCH_STATE, switchState)
            .apply()
    }
}