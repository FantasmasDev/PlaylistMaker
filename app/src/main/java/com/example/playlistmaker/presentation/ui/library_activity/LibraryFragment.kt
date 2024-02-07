package com.example.playlistmaker.presentation.ui.library_activity

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LibraryFragment : Fragment() {

    private val vm: LibraryFragmentViewModel by viewModel {
        parametersOf(requireArguments().getInt(PLACEHOLDER_TYPE))
    }

    companion object {
        private const val PLACEHOLDER_TYPE = "placeholder_type"

        fun newInstance(number: Int) = LibraryFragment().apply {
            arguments = Bundle().apply {
                putInt(PLACEHOLDER_TYPE, number)
            }
        }
    }

    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        conteiner: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, conteiner, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPlaceHolderImage()

        vm.getFragmentState().observe(viewLifecycleOwner, Observer {
            when (it.fragmentType) {
                1 -> {
                    binding.placeholderText.text = requireActivity().getString(R.string.empty_library)
                    binding.newPlaylistButton.isVisible = false
                }

                2 -> binding.placeholderText.text = requireActivity().getString(R.string.empty_playlist_list)
                else -> binding.placeholderText.text =
                    requireActivity().getString(R.string.something_went)

            }
        })

//        when (requireArguments().getInt(PLACEHOLDER_TYPE).toString()) {
//            "1" -> {
//                binding.placeholderText.text = "Ваша медиатека пуста"
//                binding.newPlaylistButton.isVisible = false
//            }
//
//            "2" -> binding.placeholderText.text = "Вы не создали ни одного плейлиста"
//            else -> binding.placeholderText.text = "Что-то пошло не так"
//        }
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