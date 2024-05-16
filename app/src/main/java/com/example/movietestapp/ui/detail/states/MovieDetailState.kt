package com.example.movietestapp.ui.detail.states

import com.example.movietestapp.data.dto.MovieData

data class MovieDetailState(
    val movieDetail: MovieData? = null
)

data class MovieDetailStateWrapper(
    val movieDetailState: MovieDetailState?
)
