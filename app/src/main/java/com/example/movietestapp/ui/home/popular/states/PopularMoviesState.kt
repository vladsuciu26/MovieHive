package com.example.movietestapp.ui.home.popular.states

import com.example.movietestapp.data.dto.MovieData

data class PopularMoviesState(
    val popularMovies: ArrayList<MovieData>? = null
)

data class PopularMoviesStateWrapper(
    val popularMoviesState: PopularMoviesState?
)