package com.example.playlistmaker.presentation.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.app.App
import com.example.playlistmaker.presentation.ui.settings_activity.SettingsActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.presentation.ui.library_activity.LibraryActivity
import com.example.playlistmaker.presentation.ui.search_activity.SearchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm by viewModel<MainViewModel>()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.readTheme()
        vm.getViewState().observe(this) {
            (applicationContext as App).switcherTheme(it.currentThemeState.themeState)
        }

        binding.apply {
            searchButton.setOnClickListener {
                openScreen(SearchActivity::class.java)
            }

            libraryButton.setOnClickListener {
                openScreen(LibraryActivity::class.java)
            }

            settingButton.setOnClickListener {
                openScreen(SettingsActivity::class.java)
            }
        }
    }

    private fun openScreen(activity: Class<*>) {
        val intent = Intent(this@MainActivity, activity)
        startActivity(intent)
    }
}