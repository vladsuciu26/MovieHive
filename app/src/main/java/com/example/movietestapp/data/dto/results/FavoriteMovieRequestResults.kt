package com.example.movietestapp.data.dto.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteMovieRequestResults(
    @SerialName("media_type")
    val mediaType: String,

    @SerialName("media_id")
    val mediaId: Int,

    @SerialName("favorite")
    val favorite: Boolean
)

@Serializable
data class FavoriteResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("status_code")
    val statusCode: Int,

    @SerialName("status_message")
    val statusMessage: String
)
