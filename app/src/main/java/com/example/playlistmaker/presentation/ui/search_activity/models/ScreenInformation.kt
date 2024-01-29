package com.example.playlistmaker.presentation.ui.search_activity.models

import com.example.playlistmaker.domain.models.TrackDomain

class ScreenInformation(
    var screenState: SearchViewState,
    var history: ArrayList<TrackDomain>
)