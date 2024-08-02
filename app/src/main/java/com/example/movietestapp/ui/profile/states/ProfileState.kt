package com.example.movietestapp.ui.profile.states

data class ProfileState(
    val userId: String? = null,
    val name: String? = null,
    val memberSince: String? = null,
    val followers: String? = null,
    val following: String? = null,
    val note: String? = null,
    val interests: String? = null,
    val contactEmail: String? = null,
    val contactPhone: String? = null
)

data class ProfileStateWrapper (
    val profileState: ProfileState?
)
