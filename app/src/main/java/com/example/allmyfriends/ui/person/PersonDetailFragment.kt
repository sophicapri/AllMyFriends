package com.example.allmyfriends.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.allmyfriends.R
import com.example.allmyfriends.databinding.FragmentPersonDetailBinding
import dagger.hilt.android.AndroidEntryPoint

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
        bindViews()
    }

    private fun bindViews() {
        val person = PersonDetailFragmentArgs.fromBundle(requireArguments()).person
        Glide.with(requireContext())
            .load(person.picture.large)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.profilePicture)
        with(binding) {
            toolbarTitle.title =
                getString(R.string.person_name, person.name.first, person.name.last)
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
            location.text = getString(
                R.string.location,
                person.location.street.number,
                person.location.street.name,
                person.location.city,
                person.location.postcode,
                person.location.state
            )
            nationality.text = person.nationality
            birthday.text = person.dateOfBirth.formattedDate()
            gender.text = person.gender.replaceFirstChar { it.titlecase() }
            email.text = person.email
            cellPhone.text = person.cell
            homePhone.text = person.phone
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PersonDetailFragment"
    }
}