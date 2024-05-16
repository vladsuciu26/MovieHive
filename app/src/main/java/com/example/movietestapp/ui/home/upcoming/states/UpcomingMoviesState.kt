package com.example.movietestapp.ui.home.upcoming.states

import com.example.movietestapp.data.dto.MovieData

data class UpcomingMoviesState(
    val upcomingMovies: ArrayList<MovieData>? = null
)

data class UpcomingMoviesStateWrapper(
    val upcomingMoviesState: UpcomingMoviesState?
)