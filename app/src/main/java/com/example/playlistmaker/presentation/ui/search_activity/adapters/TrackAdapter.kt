package com.example.playlistmaker.presentation.ui.search_activity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.presentation.ui.search_activity.holders.TrackViewHolder

class TrackAdapter(
    private val clickListener: TrackClickListener,
) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<TrackDomain>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], clickListener)
    }

    override fun getItemCount() = tracks.size

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackDomain)
    }
}