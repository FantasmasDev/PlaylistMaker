package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.models.TrackURLDataModel
import com.example.playlistmaker.domain.models.TrackURLDomainModel

object DataMapper {
    fun mapToStorageTrackModel(track: TrackURLDomainModel): TrackURLDataModel {
        return TrackURLDataModel(
            previewUrl = track.previewUrl
        )
    }
    fun mapToDomainTrackModel(track: TrackURLDataModel): TrackURLDomainModel {
        return TrackURLDomainModel(
            previewUrl = track.previewUrl
        )
    }
}