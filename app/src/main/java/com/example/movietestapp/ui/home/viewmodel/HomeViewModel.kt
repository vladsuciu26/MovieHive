package com.example.movietestapp.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietestapp.data.repositories.PopularMoviesRepository
import com.example.movietestapp.data.repositories.TopRatedMoviesRepository
import com.example.movietestapp.data.repositories.UpcomingMoviesRepository
import com.example.movietestapp.ui.home.popular.states.PopularMoviesState
import com.example.movietestapp.ui.home.popular.states.PopularMoviesStateWrapper
import com.example.movietestapp.ui.home.toprated.states.TopRatedMoviesState
import com.example.movietestapp.ui.home.toprated.states.TopRatedMoviesStateWrapper
import com.example.movietestapp.ui.home.upcoming.states.UpcomingMoviesState
import com.example.movietestapp.ui.home.upcoming.states.UpcomingMoviesStateWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val popularMoviesRepository: PopularMoviesRepository = PopularMoviesRepository()
    private val topRatedMoviesRepository: TopRatedMoviesRepository = TopRatedMoviesRepository()
    private val upcomingMoviesRepository: UpcomingMoviesRepository = UpcomingMoviesRepository()

    private val _uiPopularMoviesState = MutableStateFlow(PopularMoviesStateWrapper(null))
    val uiPopularMoviesState: StateFlow<PopularMoviesStateWrapper> = _uiPopularMoviesState.asStateFlow()

    private val _uiTopRatedMoviesState = MutableStateFlow(TopRatedMoviesStateWrapper(null))
    val uiTopRatedMoviesState: StateFlow<TopRatedMoviesStateWrapper> = _uiTopRatedMoviesState.asStateFlow()

    private val _uiUpcomingMoviesState = MutableStateFlow(UpcomingMoviesStateWrapper(null))
    val uiUpcomingMoviesState: StateFlow<UpcomingMoviesStateWrapper> = _uiUpcomingMoviesState.asStateFlow()

    fun loadPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val popularMoviesResponse = popularMoviesRepository.fetchPopularMoviesResponse()
            Log.d("PopularViewModel", "loadPopularMovies: $popularMoviesResponse")
            if (popularMoviesResponse != null) {
                _uiPopularMoviesState.update { currentState ->
                    currentState.copy(
                        popularMoviesState = PopularMoviesState(popularMoviesResponse.results)
                    )
                }
            }
        }
    }

    fun loadTopRatedMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val topRatedMoviesResponse = topRatedMoviesRepository.fetchTopRatedMoviesResponse()
            Log.d("PopularViewModel", "loadTopRatedMovies: $topRatedMoviesResponse")
            if (topRatedMoviesResponse != null) {
                _uiTopRatedMoviesState.update { currentState ->
                    currentState.copy(
                        topRatedMoviesState = TopRatedMoviesState(topRatedMoviesResponse.results)
                    )
                }
            }
        }
    }

    fun loadUpcomingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val upcomingMoviesResponse = upcomingMoviesRepository.fetchUpcomingMoviesResponse()
            Log.d("PopularViewModel", "loadUpcomingMovies: $upcomingMoviesResponse")
            if (upcomingMoviesResponse != null) {
                _uiUpcomingMoviesState.update { currentState ->
                    currentState.copy(
                        upcomingMoviesState = UpcomingMoviesState(upcomingMoviesResponse.results)
                    )
                }
            }
        }
    }

}
