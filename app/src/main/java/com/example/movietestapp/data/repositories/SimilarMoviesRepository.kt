package com.example.movietestapp.data.repositories

import com.example.movietestapp.data.api.SimilarMoviesApi
import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.data.retrofit.RetrofitHelper

class SimilarMoviesRepository {
    private val apiService = RetrofitHelper.buildService(SimilarMoviesApi::class.java)

    suspend fun fetchSimilarMovies(movieId: Int) : MoviesRequestResults {
        val movieDetailResults = apiService.getSimilarMovies(movieId, 1)
        return movieDetailResults
    }
}