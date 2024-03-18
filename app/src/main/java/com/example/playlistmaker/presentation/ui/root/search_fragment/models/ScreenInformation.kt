package com.example.playlistmaker.presentation.ui.root.search_fragment.models

import com.example.playlistmaker.domain.models.TrackDomain

class ScreenInformation(
    var screenState: SearchFragmentState,
    var history: ArrayList<TrackDomain>
)