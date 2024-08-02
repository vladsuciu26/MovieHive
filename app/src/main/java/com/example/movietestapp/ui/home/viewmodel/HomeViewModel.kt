package com.example.movietestapp.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietestapp.data.repositories.FilterMovieRepository
import com.example.movietestapp.data.repositories.PopularMoviesRepository
import com.example.movietestapp.data.repositories.SearchMoviesRepository
import com.example.movietestapp.data.repositories.TopRatedMoviesRepository
import com.example.movietestapp.data.repositories.UpcomingMoviesRepository
import com.example.movietestapp.ui.home.popular.states.PopularMoviesState
import com.example.movietestapp.ui.home.popular.states.PopularMoviesStateWrapper
import com.example.movietestapp.ui.home.search.SearchMoviesState
import com.example.movietestapp.ui.home.search.SearchMoviesStateWrapper
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
    private val searchMoviesRepository: SearchMoviesRepository = SearchMoviesRepository()
    private val filterMoviesRepository: FilterMovieRepository = FilterMovieRepository()

    private val _uiPopularMoviesState = MutableStateFlow(PopularMoviesStateWrapper(null))
    val uiPopularMoviesState: StateFlow<PopularMoviesStateWrapper> = _uiPopularMoviesState.asStateFlow()

    private val _uiTopRatedMoviesState = MutableStateFlow(TopRatedMoviesStateWrapper(null))
    val uiTopRatedMoviesState: StateFlow<TopRatedMoviesStateWrapper> = _uiTopRatedMoviesState.asStateFlow()

    private val _uiUpcomingMoviesState = MutableStateFlow(UpcomingMoviesStateWrapper(null))
    val uiUpcomingMoviesState: StateFlow<UpcomingMoviesStateWrapper> = _uiUpcomingMoviesState.asStateFlow()

    private val _uiSearchMoviesState = MutableStateFlow(SearchMoviesStateWrapper(null))
    val uiSearchMoviesState: StateFlow<SearchMoviesStateWrapper> = _uiSearchMoviesState.asStateFlow()

    private var currentPagePopular = 1
    private var currentPageTopRated = 1
    private var currentPageUpcoming = 1

    private var filterYear: Int? = null
    private var filterSortBy: String? = null
    private var filterGenres: String? = null
    private var filterKeywords: String? = null

    fun loadPopularMovies(page: Int = 1) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val popularMoviesResponse = popularMoviesRepository.fetchPopularMoviesResponse(page)
                Log.d("PopularViewModel", "loadPopularMovies: $popularMoviesResponse")
                if (popularMoviesResponse != null) {
                    _uiPopularMoviesState.update { currentState ->
                        currentState.copy(
                            popularMoviesState = PopularMoviesState(popularMoviesResponse.results)
                        )
                    }
                    currentPagePopular = page
                    Log.d("HomeViewModel", "Popular movies loaded successfully")
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "loadPopularMovies: $e")
        }
    }

    fun loadTopRatedMovies(page: Int = 1) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val topRatedMoviesResponse = topRatedMoviesRepository.fetchTopRatedMoviesResponse(page)
                Log.d("PopularViewModel", "loadTopRatedMovies: $topRatedMoviesResponse")
                if (topRatedMoviesResponse != null) {
                    _uiTopRatedMoviesState.update { currentState ->
                        currentState.copy(
                            topRatedMoviesState = TopRatedMoviesState(topRatedMoviesResponse.results)
                        )
                    }
                    currentPageTopRated = page
                    Log.d("HomeViewModel", "Top rated movies loaded successfully")
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "loadTopRatedMovies: $e")
        }
    }

    fun loadUpcomingMovies(page: Int = 1) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val upcomingMoviesResponse = upcomingMoviesRepository.fetchUpcomingMoviesResponse(page)
                Log.d("PopularViewModel", "loadUpcomingMovies: $upcomingMoviesResponse")
                if (upcomingMoviesResponse != null) {
                    _uiUpcomingMoviesState.update { currentState ->
                        currentState.copy(
                            upcomingMoviesState = UpcomingMoviesState(upcomingMoviesResponse.results)
                        )
                    }
                    currentPageUpcoming = page
                    Log.d("HomeViewModel", "Upcoming movies loaded successfully")
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "loadUpcomingMovies: $e")
        }
    }

    fun searchMovies(query: String, page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = searchMoviesRepository.searchMovies(query, page)
                _uiSearchMoviesState.update { currentState ->
                    currentState.copy(
                        searchMoviesState = SearchMoviesState(response.results)
                    )
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "searchMovies: ", e)
                _uiSearchMoviesState.update { currentState ->
                    currentState.copy(
                        searchMoviesState = SearchMoviesState(null)
                    )
                }
            }
        }
    }

    fun loadFilteredMovies(year: Int?, sortBy: String?, genres: String?, keywords: String?) {
        filterYear = year
        filterSortBy = sortBy
        filterGenres = genres
        filterKeywords = keywords

        loadFilteredMoviesInternal(currentPagePopular, currentPageTopRated, currentPageUpcoming)
    }

    private fun loadFilteredMoviesInternal(pagePopular: Int, pageTopRated: Int, pageUpcoming: Int) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val filteredPopularMoviesResponse = filterMoviesRepository
                    .fetchFilteredMovies(filterYear, filterSortBy, filterGenres, filterKeywords, pagePopular)
                val filteredTopRatedMoviesResponse = filterMoviesRepository
                    .fetchFilteredMovies(filterYear, filterSortBy, filterGenres, filterKeywords, pageTopRated)
                val filteredUpcomingMoviesResponse = filterMoviesRepository
                    .fetchFilteredMovies(filterYear, filterSortBy, filterGenres, filterKeywords, pageUpcoming)
                Log.d("FilteredViewModel", "loadFilteredMovies: $filteredPopularMoviesResponse")

                _uiPopularMoviesState.update { currentState ->
                    currentState.copy(
                        popularMoviesState = PopularMoviesState(filteredPopularMoviesResponse.results)
                    )
                }
                _uiTopRatedMoviesState.update { currentState ->
                    currentState.copy(
                        topRatedMoviesState = TopRatedMoviesState(filteredTopRatedMoviesResponse.results)
                    )
                }
                _uiUpcomingMoviesState.update { currentState ->
                    currentState.copy(
                        upcomingMoviesState = UpcomingMoviesState(filteredUpcomingMoviesResponse.results)
                    )
                }
                Log.d("HomeViewModel", "Filtered movies loaded successfully")
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "loadFilteredMovies: $e")
        }
    }

    fun getCurrentPagePopular(): Int = currentPagePopular
    fun getCurrentPageTopRated(): Int = currentPageTopRated
    fun getCurrentPageUpcoming(): Int = currentPageUpcoming

    fun loadNextPagePopular() {
        currentPagePopular++
        if (isFilterApplied()) {
            loadFilteredMoviesInternal(currentPagePopular, currentPageTopRated, currentPageUpcoming)
        } else {
            loadPopularMovies(currentPagePopular)
        }
    }

    fun loadNextPageTopRated() {
        currentPageTopRated++
        if (isFilterApplied()) {
            loadFilteredMoviesInternal(currentPagePopular, currentPageTopRated, currentPageUpcoming)
        } else {
            loadTopRatedMovies(currentPageTopRated)
        }
    }

    fun loadNextPageUpcoming() {
        currentPageUpcoming++
        if (isFilterApplied()) {
            loadFilteredMoviesInternal(currentPagePopular, currentPageTopRated, currentPageUpcoming)
        } else {
            loadUpcomingMovies(currentPageUpcoming)
        }
    }

    fun loadPreviousPagePopular() {
        if (currentPagePopular > 1) {
            currentPagePopular--
            if (isFilterApplied()) {
                loadFilteredMoviesInternal(currentPagePopular, currentPageTopRated, currentPageUpcoming)
            } else {
                loadPopularMovies(currentPagePopular)
            }
        }
    }

    fun loadPreviousPageTopRated() {
        if (currentPageTopRated > 1) {
            currentPageTopRated--
            if (isFilterApplied()) {
                loadFilteredMoviesInternal(currentPagePopular, currentPageTopRated, currentPageUpcoming)
            } else {
                loadTopRatedMovies(currentPageTopRated)
            }
        }
    }

    fun loadPreviousPageUpcoming() {
        if (currentPageUpcoming > 1) {
            currentPageUpcoming--
            if (isFilterApplied()) {
                loadFilteredMoviesInternal(currentPagePopular, currentPageTopRated, currentPageUpcoming)
            } else {
                loadUpcomingMovies(currentPageUpcoming)
            }
        }
    }

    private fun isFilterApplied(): Boolean {
        return filterYear != null || filterSortBy != null || filterGenres != null || filterKeywords != null
    }
}