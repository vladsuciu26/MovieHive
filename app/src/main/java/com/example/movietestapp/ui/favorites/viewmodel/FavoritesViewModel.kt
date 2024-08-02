package com.example.movietestapp.ui.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietestapp.data.repositories.FavoritesRepository
import com.example.movietestapp.ui.favorites.states.FavoriteMoviesState
import com.example.movietestapp.ui.favorites.states.FavoriteMoviesStateWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val favoriteMoviesRepository: FavoritesRepository = FavoritesRepository()

    private val _uiFavoriteMovieState = MutableStateFlow(FavoriteMoviesStateWrapper(null))
    val uiFavoriteMovieState: StateFlow<FavoriteMoviesStateWrapper> = _uiFavoriteMovieState.asStateFlow()

    fun loadFavoriteMovies(accountId: Int, sessionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteMoviesResponse = favoriteMoviesRepository.getFavoriteMovies(accountId, sessionId)
            if (favoriteMoviesResponse != null) {
                _uiFavoriteMovieState.update { currentState ->
                    currentState.copy(
                        favoriteMoviesState = FavoriteMoviesState(favoriteMoviesResponse.results)
                    )
                }
            }
        }
    }
}