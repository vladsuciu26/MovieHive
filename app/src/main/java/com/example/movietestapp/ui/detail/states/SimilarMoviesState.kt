package com.example.movietestapp.ui.detail.states

import com.example.movietestapp.data.dto.MovieData

data class SimilarMoviesState(
    val similarMovie: ArrayList<MovieData>? = null
)

data class SimilarMoviesStateWrapper(
    val similarMovieState: SimilarMoviesState?
)
