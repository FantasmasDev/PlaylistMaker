package com.example.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView
    private val albumPicView: ImageView
    private val trackButton: ImageView


    init {
        trackNameView = itemView.findViewById(R.id.track_name)
        artistNameView = itemView.findViewById(R.id.artist_name)
        trackTimeView = itemView.findViewById(R.id.track_time)
        albumPicView = itemView.findViewById(R.id.album_pic)
        trackButton = itemView.findViewById(R.id.track_action_button)
    }

    fun bind(track: Track, clickListener: TrackAdapter.TrackClickListener) {
        trackButton.setOnClickListener { clickListener.onTrackClick(track) }
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        val cornerRadiusDP = 2F
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.empty_album_pic)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(cornerRadiusDP, itemView)))
            .into(albumPicView)

//        trackButton.setOnClickListener {
//            SearchHistory.add(track)
//        }
    }

    fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}
