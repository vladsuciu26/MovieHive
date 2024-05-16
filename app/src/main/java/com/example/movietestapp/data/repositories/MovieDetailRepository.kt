package com.example.movietestapp.data.repositories

import com.example.movietestapp.data.api.MovieDetailApi
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.data.retrofit.RetrofitHelper

class MovieDetailRepository {
    private val apiService = RetrofitHelper.buildService(MovieDetailApi::class.java)

    suspend fun fetchMovieDetail(movieId: Int) : MovieData {
        val movieDetailResults = apiService.getMovieDetail(movieId, 1)
        return movieDetailResults
    }
}