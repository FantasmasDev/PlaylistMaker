package com.example.playlistmaker.presentation.ui.root.library_fragment.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.presentation.ui.root.library_fragment.fragments.library_fragment.LibraryFragment
import com.example.playlistmaker.presentation.ui.root.library_fragment.fragments.playlist_fragment.PlayListLibraryFragment


class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LibraryFragment.newInstance()
            1 -> PlayListLibraryFragment.newInstance()
            else -> Fragment()
        }
    }
}