package com.example.movietestapp.data.repositories

import com.example.movietestapp.data.api.MovieDetailApi
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.data.dto.details.DetailsData
import com.example.movietestapp.data.dto.results.MovieVideosResponse
import com.example.movietestapp.data.retrofit.RetrofitHelper
import com.example.movietestapp.util.Constants

class MovieDetailRepository {
    private val apiService = RetrofitHelper.buildService(MovieDetailApi::class.java)

    suspend fun fetchMovieDetail(movieId: Int) : DetailsData {
        val movieDetailResults = apiService.getMovieDetail(movieId)
        return movieDetailResults
    }

    suspend fun fetchMovieVideos(movieId: Int): MovieVideosResponse {
        return apiService.getMovieVideos(movieId, Constants.API_KEY)
    }
}