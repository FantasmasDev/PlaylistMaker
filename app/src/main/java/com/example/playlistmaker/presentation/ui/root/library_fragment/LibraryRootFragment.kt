package com.example.playlistmaker.presentation.ui.root.library_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryRootBinding
import com.example.playlistmaker.presentation.ui.root.library_fragment.adapters.LibraryViewPagerAdapter
import com.example.playlistmaker.presentation.ui.root.settings_fragment.SettingsFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryRootFragment : Fragment() {
    private lateinit var binding: FragmentLibraryRootBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val vm: LibraryRootFragmentModel by viewModel<LibraryRootFragmentModel>()

    companion object {
        fun newInstance() = LibraryRootFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.libraryViewPager.adapter =
            LibraryViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabs, binding.libraryViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.library_tracks_tab)
                1 -> tab.text = getString(R.string.playlist_tab)
            }
        }

        tabMediator.attach()

//        binding.menuButton.setOnClickListener { finish() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}