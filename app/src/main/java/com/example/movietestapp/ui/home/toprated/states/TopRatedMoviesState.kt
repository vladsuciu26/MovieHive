package com.example.movietestapp.ui.home.toprated.states

import com.example.movietestapp.data.dto.MovieData

data class TopRatedMoviesState(
    val topRatedMovies: ArrayList<MovieData>? = null
)

data class TopRatedMoviesStateWrapper(
    val topRatedMoviesState: TopRatedMoviesState?
)