package com.example.playlistmaker.presentation.ui.root.search_fragment.holders

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackDomain
import com.example.playlistmaker.presentation.ui.root.search_fragment.adapters.TrackAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView
    private val albumPicView: ImageView


    init {
        trackNameView = itemView.findViewById(R.id.track_name)
        artistNameView = itemView.findViewById(R.id.artist_name)
        trackTimeView = itemView.findViewById(R.id.track_time)
        albumPicView = itemView.findViewById(R.id.album_pic)
    }

    fun bind(track: TrackDomain, clickListener: TrackAdapter.TrackClickListener) {
        itemView.setOnClickListener { clickListener.onTrackClick(track) }
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        val cornerRadiusDP = 2F
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.empty_album_pic)
            .transform(CenterCrop(), RoundedCorners(dpToPx(cornerRadiusDP, itemView)))
            .into(albumPicView)
    }

    fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}
