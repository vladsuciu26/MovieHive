package com.example.movietestapp.ui.home.search

import com.example.movietestapp.data.dto.MovieData

data class SearchMoviesState(
    val searchMovies: ArrayList<MovieData>? = null
)

data class SearchMoviesStateWrapper(
    val searchMoviesState: SearchMoviesState?
)