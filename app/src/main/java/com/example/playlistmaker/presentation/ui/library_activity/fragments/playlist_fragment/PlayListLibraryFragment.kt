package com.example.playlistmaker.presentation.ui.library_activity.fragments.playlist_fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistLibraryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListLibraryFragment : Fragment() {

    private val vm: PlayListLibraryFragmentViewModel by viewModel<PlayListLibraryFragmentViewModel>()

    companion object {
        fun newInstance() = PlayListLibraryFragment()
            }


    private lateinit var binding: FragmentPlaylistLibraryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        conteiner: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistLibraryBinding.inflate(inflater, conteiner, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPlaceHolderImage()
    }

    private fun setPlaceHolderImage() {
        val placeholderImage = binding.placeholderImage

        fun getPlaceholderResourceId(attributeId: Int): Int {
            val typedValue = TypedValue()
            requireActivity().theme.resolveAttribute(attributeId, typedValue, true)
            return typedValue.resourceId
        }
        placeholderImage.setImageResource(getPlaceholderResourceId(R.attr.placeholder_image_not_found))
    }

}