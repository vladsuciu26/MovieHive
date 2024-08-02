package com.example.movietestapp.ui.detail.states

import com.example.movietestapp.data.dto.details.DetailsData

data class MovieDetailState(
    val movieDetail: DetailsData? = null
)

data class MovieDetailStateWrapper(
    val movieDetailState: MovieDetailState?
)
