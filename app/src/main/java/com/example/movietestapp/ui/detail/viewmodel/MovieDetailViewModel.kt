package com.example.movietestapp.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietestapp.data.repositories.MovieDetailRepository
import com.example.movietestapp.ui.detail.states.MovieDetailState
import com.example.movietestapp.ui.detail.states.MovieDetailStateWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val movieDetailRepository: MovieDetailRepository = MovieDetailRepository()
    private val _uiMovieDetailState = MutableStateFlow(MovieDetailStateWrapper(null))
    val uiMovieDetailState: StateFlow<MovieDetailStateWrapper> = _uiMovieDetailState.asStateFlow()

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
}