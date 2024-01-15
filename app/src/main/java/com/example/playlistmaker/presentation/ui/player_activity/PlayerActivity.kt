package com.example.playlistmaker.presentation.ui.player_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.models.Track
import com.example.playlistmaker.databinding.PlayerLayoutBinding
import com.example.playlistmaker.domain.models.PlayerStateDomain
import com.example.playlistmaker.presentation.mapper.TrackMapper
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var vm: PlayerViewModel

    private lateinit var binding: PlayerLayoutBinding

    private lateinit var track: Track

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getParcelableExtra("track")!!

        vm = ViewModelProvider(this, PlayerViewModelFactory(track, this)).get(PlayerViewModel::class.java)

        vm.timerText.observe(this, Observer{
            binding.playerDuration.text = it
        })

        vm.isPlay.observe(this, Observer{
            setPlayButtonIcon(it)
        })

        vm.preparePlayer()

        // TODO придумать перенос хандер в вм
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
                vm.playbackControl()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        vm.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.resetTimer()
        vm.release()
    }

    private fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun setPlayButtonIcon(
        isPlay: Boolean
    ) {
        val attributeId = if (isPlay){
            R.attr.pause_button
        } else {
            R.attr.play_button
        }

        val typedValue = TypedValue()
        theme.resolveAttribute(attributeId, typedValue, true)
        typedValue.resourceId
        binding.playerPlayButton.setBackgroundResource(typedValue.resourceId)
    }
}