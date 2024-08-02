package com.example.movietestapp.data.dto.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideosResponse(
    @SerialName("results")
    val results: List<Video>
)

@Serializable
data class Video(
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("site")
    val site: String,
    @SerialName("type")
    val type: String
)