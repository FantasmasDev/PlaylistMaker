package com.example.playlistmaker.presentation.mapper


import com.example.playlistmaker.domain.models.CurrentTimeDomainModel
import com.example.playlistmaker.domain.models.TrackURLDomainModel
import com.example.playlistmaker.presentation.models.CurrentTimePresentationModel
import com.example.playlistmaker.presentation.models.Track

object TrackMapper {
    fun mapToDomain(track: Track): TrackURLDomainModel{
        return TrackURLDomainModel(
            previewUrl = track.previewUrl
        )
    }

    fun mapToCurrentTimePresentationModel(currentTimeDomainModel: CurrentTimeDomainModel):CurrentTimePresentationModel{
        return CurrentTimePresentationModel(
            time = currentTimeDomainModel.time
        )
    }
}