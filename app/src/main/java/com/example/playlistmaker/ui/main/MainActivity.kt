package com.example.playlistmaker.ui.main

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.App
import com.example.playlistmaker.ui.settings_activity.SettingsAcivity
import com.example.playlistmaker.UserPreferences
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.library_activity.LibraryActivity
import com.example.playlistmaker.ui.search_activity.SearchActivity

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

        binding.apply {
            searchButton.setOnClickListener {
                openScreen(SearchActivity::class.java)
            }

            libraryButton.setOnClickListener {
                openScreen(LibraryActivity::class.java)
            }

            settingButton.setOnClickListener {
                openScreen(SettingsAcivity::class.java)
            }
        }
    }

    private fun openScreen(activity: Class<*>){
        val intent = Intent(this@MainActivity, activity)
        startActivity(intent)
    }
}