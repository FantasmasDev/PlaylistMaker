package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsAcivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    val userPreferences = UserPreferences()

    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var themeSwitcher: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)

        themeSwitcher = binding.themeSwitcher

        if(userPreferences.readSwitchState(sharedPreferences)) {
            themeSwitcher.isChecked = true
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked -> (applicationContext as App).switcherTheme(checked)}

        binding.settingHomeButton.setOnClickListener {
            finish()
        }

        binding.sharingAppButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.link)
            )
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.share_app_link)
                )
            )
        }

        binding.supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_mail)))
            supportIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.mail_theme)
            )
            supportIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.mail_content)
            )
            startActivity(supportIntent)
        }

        binding.termsOfUseButton.setOnClickListener {
            val termsIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_link)))
            startActivity(termsIntent)
        }
    }

    override fun onStop() {
        val switchState = themeSwitcher.isChecked
        userPreferences.writeTheme(sharedPreferences, switchState)
        super.onStop()
    }
}