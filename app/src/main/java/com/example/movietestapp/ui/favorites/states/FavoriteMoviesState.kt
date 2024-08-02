package com.example.movietestapp.ui.favorites.states

import com.example.movietestapp.data.dto.MovieData

data class FavoriteMoviesState(
    val favoriteMovie: ArrayList<MovieData>? = null
)

data class FavoriteMoviesStateWrapper(
    val favoriteMoviesState: FavoriteMoviesState?
)
