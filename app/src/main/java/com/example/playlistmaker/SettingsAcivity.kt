package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsAcivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}