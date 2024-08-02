package com.example.movietestapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietestapp.databinding.FragmentProfileBinding
import com.example.movietestapp.databinding.DialogEditProfileBinding
import com.example.movietestapp.ui.Navigator
import com.example.movietestapp.ui.adapter.ProfileRepliesAdapter
import com.example.movietestapp.ui.adapter.ProfileReviewsAdapter
import com.example.movietestapp.ui.profile.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.uiProfileState.collect { stateWrapper ->
                stateWrapper?.profileState?.let { profileState ->
                    binding.profileName.text = profileState.name
                    binding.followersCount.text = profileState.followers
                    binding.followingCount.text = profileState.following
                    binding.noteContent.text = profileState.note
                    binding.interestsContent.text = profileState.interests
                    binding.contactEmail.text = "Email: ${profileState.contactEmail}"
                    binding.contactPhone.text = "Phone: ${profileState.contactPhone}"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userReviews.collect { reviews ->
                val adapter = ProfileReviewsAdapter(reviews) { review ->
                    Navigator.getInstance().openMovieDetailFragment(review.movieId)
                }
                binding.reviewsProfileRecyclerview.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.reviewsProfileRecyclerview.adapter = adapter
            }
        }

        lifecycleScope.launch {
            viewModel.userReplies.collect { replies ->
                val adapter = ProfileRepliesAdapter(replies) { reply ->
                    Navigator.getInstance().openMovieDetailFragment(reply.movieId)
                }
                binding.repliesProfileRecyclerview.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.repliesProfileRecyclerview.adapter = adapter
            }
        }

        viewModel.loadProfile()

        binding.editProfileButton.setOnClickListener {
            showEditProfileDialog()
        }
    }

    private fun showEditProfileDialog() {
        val dialogBinding = DialogEditProfileBinding.inflate(LayoutInflater.from(requireContext()))
        val currentProfile = viewModel.uiProfileState.value?.profileState

        // Prepopulate the dialog with current profile data
        dialogBinding.editNote.setText(currentProfile?.note)
        dialogBinding.editInterests.setText(currentProfile?.interests)
        dialogBinding.editContactEmail.setText(currentProfile?.contactEmail)
        dialogBinding.editContactPhone.setText(currentProfile?.contactPhone)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Profile")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val note = dialogBinding.editNote.text.toString().takeIf { it.isNotBlank() }
                val interests = dialogBinding.editInterests.text.toString().takeIf { it.isNotBlank() }
                val contactEmail = dialogBinding.editContactEmail.text.toString().takeIf { it.isNotBlank() }
                val contactPhone = dialogBinding.editContactPhone.text.toString().takeIf { it.isNotBlank() }

                viewModel.updateProfile(note, interests, contactEmail, contactPhone)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}