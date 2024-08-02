package com.example.movietestapp.ui.favorites.states

import com.example.movietestapp.data.dto.results.FavoriteResponse

//data class AddFavoriteMoviesState(
//    val addFavoriteMovie: String? = null
//)
//
//data class AddFavoriteMoviesStateWrapper(
//    val addFavoriteMoviesState: AddFavoriteMoviesState?
//)

data class AddFavoriteMoviesState(
    val favoriteResponse: FavoriteResponse?
)

data class AddFavoriteMoviesStateWrapper(
    val addFavoriteMoviesState: AddFavoriteMoviesState?
)