package com.example.movietestapp.data.dto.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDetail(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("backdrop_path")
    val backdropPath: String?
)
