package com.example.playlistmaker.presentation.ui.player_activity

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
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.model.Track
import com.example.playlistmaker.databinding.PlayerLayoutBinding
import com.example.playlistmaker.domain.models.PlayerStateDomain
import com.example.playlistmaker.presentation.mapper.TrackMapper
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private val playerEntity = Creator.providePlayerEntity()

    companion object {
        private const val SECOND = 1000L
        private const val HALF_SECOND = 500L
        private const val THIRD_OF_SECOND = 333L
    }

    private var timerRunnable: Runnable? = null


    private lateinit var binding: PlayerLayoutBinding


    private lateinit var track: Track

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
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetTimer()
        playerEntity.release()
    }

    fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun preparePlayer() {
        playerEntity.setOnPreparedListener(::preparedListener)
        playerEntity.setOnCompletionListener(::completionListener)
        playerEntity.prepare(track)
    }

    private fun preparedListener() {
        playerEntity.setState(PlayerStateDomain.STATE_PREPARED)
    }

    private fun completionListener() {
        playerEntity.setState(PlayerStateDomain.STATE_PREPARED)
        resetTimer()
        binding.playerDuration.setText(R.string.player_track_length)
        setPlayButtonIcon(R.attr.play_button)
    }

    private fun startPlayer() {
        playerEntity.play()
        handler.postDelayed({ updateTimer() }, HALF_SECOND)
        setPlayButtonIcon(R.attr.pause_button)
        playerEntity.setState(PlayerStateDomain.STATE_PLAYING)
    }

    private fun pausePlayer() {
        resetTimer()
        playerEntity.pause()
        setPlayButtonIcon(R.attr.play_button)
        playerEntity.setState(PlayerStateDomain.STATE_PAUSED)
    }

    private fun playbackControl() {
        when (
            playerEntity.getCurrentState()
        ) {
            PlayerStateDomain.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStateDomain.STATE_PREPARED, PlayerStateDomain.STATE_PAUSED -> {
                startPlayer()
            }
            else -> {
                return
            }
        }
    }

    private fun setPlayButtonIcon(attributeId: Int) {
        val typedValue = TypedValue()
        theme.resolveAttribute(attributeId, typedValue, true)
        typedValue.resourceId
        binding.playerPlayButton.setBackgroundResource(typedValue.resourceId)
    }

    private fun updateTimer() {
        if (
            playerEntity.isPlaying()
            && playerEntity.getCurrentState() == PlayerStateDomain.STATE_PLAYING
        ) {
            timerRunnable = object : Runnable {
                override fun run() {
                    binding.playerDuration.text =
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(
                            TrackMapper.mapToCurrentTimePresentationModel(playerEntity.getCurrentPosition()).time
                        )

                    handler.postDelayed(this, THIRD_OF_SECOND)
                }
            }
            handler.post(timerRunnable!!)
        }
    }

    private fun resetTimer() {
        if (timerRunnable != null && (playerEntity.getCurrentState() == PlayerStateDomain.STATE_PAUSED
                    ||
                    playerEntity.getCurrentState() == PlayerStateDomain.STATE_PREPARED
                    ) &&

            !playerEntity.isPlaying()
        ) {
            handler.removeCallbacksAndMessages(null)
            timerRunnable = null
        }
    }
}