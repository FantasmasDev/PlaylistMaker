package com.example.playlistmaker.presentation.ui.root.settings_fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.app.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.models.ThemeStateParam
import com.example.playlistmaker.presentation.ui.root.search_fragment.SearchFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val vm by viewModel<SettingsFragmentModel>()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.readTheme()
        vm.getViewState().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it.currentThemeState.themeState
        }

        binding.apply {
            themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
                vm.writeTheme(ThemeStateParam(checked))
                (requireActivity().applicationContext as App).switcherTheme(checked)
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
