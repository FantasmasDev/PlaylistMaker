package com.example.playlistmaker

import android.content.SharedPreferences

object SearchHistory {
    private var historyTrackList = ArrayList<Track>()

    fun setHistory(listFromHistory: ArrayList<Track>){
        historyTrackList = listFromHistory
    }

    fun getHistory(): ArrayList<Track>{
        return historyTrackList
    }

    fun add(track: Track) {
        if(historyTrackList.contains(track)){
            historyTrackList.remove(track)
        }
        historyTrackList.add(track)
    }

    fun clean() {
        historyTrackList.clear()
    }

    fun remove(track: Track){
        historyTrackList.remove(track)
    }
}