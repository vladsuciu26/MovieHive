package com.example.movietestapp.ui.detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietestapp.data.repositories.FavoritesRepository
import com.example.movietestapp.data.repositories.MovieDetailRepository
import com.example.movietestapp.data.repositories.SimilarMoviesRepository
import com.example.movietestapp.ui.detail.states.MovieDetailState
import com.example.movietestapp.ui.detail.states.MovieDetailStateWrapper
import com.example.movietestapp.ui.detail.states.SimilarMoviesState
import com.example.movietestapp.ui.detail.states.SimilarMoviesStateWrapper
import com.example.movietestapp.ui.favorites.states.AddFavoriteMoviesState
import com.example.movietestapp.ui.favorites.states.AddFavoriteMoviesStateWrapper
import com.example.movietestapp.ui.favorites.states.FavoriteMoviesState
import com.example.movietestapp.ui.favorites.states.FavoriteMoviesStateWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val movieDetailRepository: MovieDetailRepository = MovieDetailRepository()
    private val similarMoviesRepository: SimilarMoviesRepository = SimilarMoviesRepository()
    private val favoritesRepository: FavoritesRepository = FavoritesRepository()

    private val _uiMovieDetailState = MutableStateFlow(MovieDetailStateWrapper(null))
    val uiMovieDetailState: StateFlow<MovieDetailStateWrapper> = _uiMovieDetailState.asStateFlow()

    private val _uiSimilarMovieState = MutableStateFlow(SimilarMoviesStateWrapper(null))
    val uiSimilarMovieState: StateFlow<SimilarMoviesStateWrapper> = _uiSimilarMovieState.asStateFlow()

    private val _uiAddFavoriteMovieResult = MutableStateFlow(AddFavoriteMoviesStateWrapper(null))
    val uiAddFavoriteMovieResult: StateFlow<AddFavoriteMoviesStateWrapper> = _uiAddFavoriteMovieResult.asStateFlow()

    private val _trailerUrl = MutableStateFlow<String?>(null)
    val trailerUrl: StateFlow<String?> = _trailerUrl.asStateFlow()
    fun loadMovieDetail(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieDetailResponse = movieDetailRepository.fetchMovieDetail(id)
            if (movieDetailResponse != null) {
                _uiMovieDetailState.update { currentState ->
                    currentState.copy(
                        movieDetailState = MovieDetailState(movieDetailResponse)
                    )
                }
            }
        }
    }

    fun loadSimilarMovies(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val similarMoviesResponse = similarMoviesRepository.fetchSimilarMovies(movieId)
            if (similarMoviesResponse != null) {
                _uiSimilarMovieState.update { currentState ->
                    currentState.copy(
                        similarMovieState = SimilarMoviesState(similarMoviesResponse.results)
                    )
                }
            }
        }
    }

    fun addToFavorites(accountId: Int, sessionId: String, movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = favoritesRepository.addToFavorites(accountId, sessionId, movieId)
            if (response.success) {
                Log.e("FavoritesRepository", "Successfully added to favorites: ${response.statusMessage}")
                _uiAddFavoriteMovieResult.update { currentState ->
                    currentState.copy(
                        addFavoriteMoviesState = AddFavoriteMoviesState(response)
                    )
                }
            } else {
                Log.e("FavoritesRepository", "Failed to add to favorites: ${response.statusMessage}")
                // Handle error
                _uiAddFavoriteMovieResult.update { currentState ->
                    currentState.copy(
                        addFavoriteMoviesState = AddFavoriteMoviesState(null)
                    )
                }
            }
        }
    }

    fun loadMovieVideos(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieVideosResponse = movieDetailRepository.fetchMovieVideos(movieId)
            val trailer = movieVideosResponse.results.find { it.type == "Trailer" && it.site == "YouTube" }
            _trailerUrl.update { "https://www.youtube.com/watch?v=${trailer?.key}" }
        }
    }

    fun resetFavoriteResult() {
        _uiAddFavoriteMovieResult.update { currentState ->
            currentState.copy(
                addFavoriteMoviesState = null
            )
        }
    }

}