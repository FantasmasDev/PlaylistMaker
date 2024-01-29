package com.example.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrackDomain(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,

    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,

    val previewUrl: String,
) : Parcelable