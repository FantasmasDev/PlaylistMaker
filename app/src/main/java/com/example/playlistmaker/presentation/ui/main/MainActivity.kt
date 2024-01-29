package com.example.playlistmaker.presentation.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.presentation.ui.settings_activity.SettingsActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.presentation.ui.library_activity.LibraryActivity
import com.example.playlistmaker.presentation.ui.search_activity.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
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