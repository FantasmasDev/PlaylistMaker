package com.example.playlistmaker.presentation.ui.settings_activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.models.ThemeStateParam

class SettingsActivity : AppCompatActivity() {
    private lateinit var vm: SettingsViewModel

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]
        vm.readTheme()
        vm.getViewState().observe(this) {
            binding.themeSwitcher.isChecked = it.currentThemeState.themeState
        }

        binding.apply {
            themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
                vm.writeTheme(ThemeStateParam(checked))
                (applicationContext as App).switcherTheme(checked)
            }
            settingHomeButton.setOnClickListener {
                finish()
            }
            sharingAppButton.setOnClickListener {
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
            supportButton.setOnClickListener {
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
            termsOfUseButton.setOnClickListener {
                val termsIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_link)))
                startActivity(termsIntent)
            }
        }
    }

    override fun onStop() {
        val switchState = binding.themeSwitcher.isChecked
        vm.writeTheme(ThemeStateParam(switchState))
        super.onStop()
    }
}
