package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.PlayerLayoutBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: PlayerLayoutBinding

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getParcelableExtra("track")!!

        binding.apply {
            playerTrackName.text = track.trackName
            playerArtistName.text = track.artistName

            durationInfo.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis.toLong())

            Glide.with(playerCover)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.empty_album_pic)
                .transform(CenterCrop(), RoundedCorners(dpToPx(8F, playerCover)))
                .into(playerCover)

            yearInfo.text = track.releaseDate.substring(0, 4)
            ganreInfo.text = track.primaryGenreName
            countryInfo.text = track.country

            if (track.collectionName.isNotEmpty()) {
                albumInfo.text = track.collectionName
            } else {
                albumInfo.isVisible = false
                albumText.isVisible = false
            }

            menuButton.setOnClickListener { finish() }
            playerAddButton.setOnClickListener { addTrackToHistory(track) }
        }
    }

    fun addTrackToHistory(track: Track) {
        SearchHistory.add(track)
    }

    fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

}