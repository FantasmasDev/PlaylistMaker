package com.example.playlistmaker.presentation.mapper


import com.example.playlistmaker.domain.models.CurrentTimeDomainModel
import com.example.playlistmaker.domain.models.TrackDomainModel
import com.example.playlistmaker.presentation.model.CurrentTimePresentationModel
import com.example.playlistmaker.presentation.model.Track

object TrackMapper {
    fun mapToDomain(track: Track): TrackDomainModel{
        return TrackDomainModel(
            previewUrl = track.previewUrl
        )
    }

    fun mapToCurrentTimePresentationModel(currentTimeDomainModel: CurrentTimeDomainModel):CurrentTimePresentationModel{
        return CurrentTimePresentationModel(
            time = currentTimeDomainModel.time
        )
    }
}