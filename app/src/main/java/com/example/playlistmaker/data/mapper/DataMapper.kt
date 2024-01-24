package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.models.TrackUrlDataModel
import com.example.playlistmaker.domain.models.TrackURLDomainModel

object DataMapper {
    fun mapToStorageTrackModel(track: TrackURLDomainModel): TrackUrlDataModel {
        return TrackUrlDataModel(
            previewUrl = track.previewUrl
        )
    }
    fun mapToDomainTrackModel(track: TrackUrlDataModel): TrackURLDomainModel {
        return TrackURLDomainModel(
            previewUrl = track.previewUrl
        )
    }
}