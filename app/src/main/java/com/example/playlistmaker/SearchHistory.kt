package com.example.playlistmaker

import com.example.playlistmaker.presentation.models.Track

object SearchHistory {
    private var historyTrackList = ArrayList<Track>()

    fun setHistory(listFromHistory: ArrayList<Track>) {
        historyTrackList = listFromHistory
    }

    fun getHistory(): ArrayList<Track> {
        return historyTrackList
    }

    fun add(track: Track) {
        if (historyTrackList.size < 10) {
            historyTrackList.remove(track)
            historyTrackList.add(0, track)
        }else{
            historyTrackList.remove(track)
            historyTrackList.removeLast()
            historyTrackList.add(0,track)
        }
    }

    fun clean() {
        historyTrackList.clear()
    }

    fun remove(track: Track) {
        historyTrackList.remove(track)
    }
}