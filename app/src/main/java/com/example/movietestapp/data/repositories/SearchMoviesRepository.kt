package com.example.movietestapp.data.repositories

import com.example.movietestapp.data.api.SearchMoviesApi
import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.data.retrofit.RetrofitHelper

class SearchMoviesRepository {
    private val apiService = RetrofitHelper.buildService(SearchMoviesApi::class.java)

    suspend fun searchMovies(query: String, page: Int): MoviesRequestResults {
        return apiService.searchMovies(query, page)
    }
}