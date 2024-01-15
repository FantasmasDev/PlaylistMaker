package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.model.PlayerData
import com.example.playlistmaker.data.model.TrackDataModel
import com.example.playlistmaker.domain.models.TrackDomainModel

object DataMapper {
    fun mapToStorageTrackModel(track: TrackDomainModel): TrackDataModel {
        return TrackDataModel(
            previewUrl = track.previewUrl
        )
    }
    fun mapToDomainTrackModel(track: TrackDataModel): TrackDomainModel {
        return TrackDomainModel(
            previewUrl = track.previewUrl
        )
    }
}