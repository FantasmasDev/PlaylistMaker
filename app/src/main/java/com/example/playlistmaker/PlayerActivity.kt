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

        val trackName = binding.playerTrackName
        val artistName = binding.playerArtistName
        val trackDuration = binding.durationInfo
        val albumCover = binding.playerCover

        val album = binding.albumInfo
        val albumText = binding.albumText
        val releaseDate = binding.yearInfo
        val genre = binding.ganreInfo
        val country = binding.countryInfo

        val menuButton = binding.menuButton
        val addLibrary = binding.playerAddButton

        track = intent.getParcelableExtra("track")!!

        menuButton.setOnClickListener { finish() }
        addLibrary.setOnClickListener { addTrackToHistory(track) }

        trackName.text = track.trackName
        artistName.text = track.artistName

        trackDuration.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis.toLong())

        if (track.collectionName.isNotEmpty()) {
            album.text = track.collectionName
        } else {
            album.isVisible = false
            albumText.isVisible = false
        }

        releaseDate.text = track.releaseDate.substring(0, 4)
        genre.text = track.primaryGenreName
        country.text = track.country

        Glide.with(albumCover)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.empty_album_pic)
            .transform(CenterCrop(), RoundedCorners(dpToPx(8F, albumCover)))
            .into(albumCover)
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