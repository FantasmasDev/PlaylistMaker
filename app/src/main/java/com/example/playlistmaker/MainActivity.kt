package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivityMainBinding

const val USER_PREFERENCES = "user_history"
const val TRACKS_HISTORY = "tracks_history"
const val THEME_SWITCH_STATE = "switch_state"

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    val userPreferences = UserPreferences()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)
        if(userPreferences.readSwitchState(sharedPreferences)){
            (applicationContext as App).switcherTheme(true)
        }

        binding.searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding.libraryButton.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        binding.settingButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsAcivity::class.java)
            startActivity(settingsIntent)
        }
    }
}