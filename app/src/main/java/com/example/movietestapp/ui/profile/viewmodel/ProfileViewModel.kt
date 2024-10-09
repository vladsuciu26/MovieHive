package com.example.movietestapp.ui.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietestapp.data.dto.ReplyData
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.data.repositories.ProfileRepository
import com.example.movietestapp.ui.profile.states.ProfileState
import com.example.movietestapp.ui.profile.states.ProfileStateWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiProfileState = MutableStateFlow(ProfileStateWrapper(null))
    val uiProfileState: StateFlow<ProfileStateWrapper?> = _uiProfileState.asStateFlow()

    private val _userReviews = MutableStateFlow<List<Pair<ReviewData, String>>>(emptyList())
    val userReviews: StateFlow<List<Pair<ReviewData, String>>> = _userReviews.asStateFlow()


    private val _userReplies = MutableStateFlow<List<ReplyData>>(emptyList())
    val userReplies: StateFlow<List<ReplyData>> = _userReplies.asStateFlow()

    private val repository = ProfileRepository()

    fun loadProfile() {
        viewModelScope.launch {
            val profileState = repository.getProfile()
            _uiProfileState.value = ProfileStateWrapper(profileState)
            profileState.userId?.let { userId ->
                loadUserReviews(userId)
                loadUserReplies(userId)
            }
        }
    }

    private fun loadUserReviews(userId: String) {
        viewModelScope.launch {
            val reviews = repository.getUserReviews(userId)
            _userReviews.value = reviews
        }
    }

    private fun loadUserReplies(userId: String) {
        viewModelScope.launch {
            val replies = repository.getUserReplies(userId)
            _userReplies.value = replies
        }
    }

    fun updateProfile(note: String?, interests: String?, contactEmail: String?, contactPhone: String?) {
        viewModelScope.launch {
            val currentProfile = repository.getProfile()
            val updatedProfile = currentProfile.copy(
                note = note ?: currentProfile.note,
                interests = interests ?: currentProfile.interests,
                contactEmail = contactEmail ?: currentProfile.contactEmail,
                contactPhone = contactPhone ?: currentProfile.contactPhone
            )
            repository.updateProfile(updatedProfile)
            loadProfile()
        }
    }
}
