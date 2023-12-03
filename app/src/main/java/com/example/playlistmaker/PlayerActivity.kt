package com.example.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val SECOND = 1000L
    }

    private var timerRunnable: Runnable? = null
//    private var isTimeAllowed = true

    private lateinit var binding: PlayerLayoutBinding

    private lateinit var track: Track

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getParcelableExtra("track")!!

        preparePlayer()
        handler = Handler(Looper.getMainLooper())

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

            playerPlayButton.setOnClickListener {
                playbackControl()
            }

            playerAddButton.setOnClickListener { addTrackToHistory(track) }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetTimer()
        mediaPlayer.release()
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

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            resetTimer()
            binding.playerDuration.setText(R.string.player_track_length)
            setPlayButtonIcon(R.attr.play_button)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        handler.postDelayed({ startTimer() }, SECOND / 2)
        setPlayButtonIcon(R.attr.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        resetTimer()
        mediaPlayer.pause()
        setPlayButtonIcon(R.attr.play_button)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun setPlayButtonIcon(attributeId: Int) {
        val typedValue = TypedValue()
        theme.resolveAttribute(attributeId, typedValue, true)
        typedValue.resourceId
        binding.playerPlayButton.setBackgroundResource(typedValue.resourceId)
    }

    private fun startTimer() {
        updateTimer()
    }

    private fun updateTimer() {
        timerRunnable = object : Runnable {
            override fun run() {
                binding.playerDuration.text =
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, SECOND / 3)
            }
        }
        handler.post(timerRunnable!!)
    }

    private fun resetTimer() {
        if (timerRunnable != null) {
            handler.removeCallbacks(timerRunnable!!)
            timerRunnable = null
        }
    }
}