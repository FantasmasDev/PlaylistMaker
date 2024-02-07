package com.example.playlistmaker.presentation.ui.library_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val vm: LibraryViewModel by viewModel<LibraryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.libraryViewPager.adapter =
            LibraryViewPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabs, binding.libraryViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.library_tracks_tab)
                1 -> tab.text = getString(R.string.playlist_tab)
            }
        }

        tabMediator.attach()

        binding.menuButton.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}