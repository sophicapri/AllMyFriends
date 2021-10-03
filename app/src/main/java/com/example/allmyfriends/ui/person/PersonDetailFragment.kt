package com.example.allmyfriends.ui.person

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.allmyfriends.databinding.FragmentPersonDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlin.math.log

@AndroidEntryPoint
class PersonDetailFragment : Fragment() {
    private var _binding: FragmentPersonDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayPerson()
    }

    private fun displayPerson() {
        val person = PersonDetailFragmentArgs.fromBundle(requireArguments()).person
        Glide.with(requireContext())
            .load(person.picture.large)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.profilePicture)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PersonDetailFragment"
    }
}