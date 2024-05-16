package com.example.movietestapp.data.repositories

import android.util.Log
import com.example.movietestapp.data.api.UpcomingMoviesApi
import com.example.movietestapp.data.dto.results.UpcomingMoviesRequestResults
import com.example.movietestapp.data.retrofit.RetrofitHelper
import com.example.movietestapp.util.Constants

class UpcomingMoviesRepository {
    private val apiService = RetrofitHelper.buildService(UpcomingMoviesApi::class.java)

    suspend fun fetchUpcomingMoviesResponse() : UpcomingMoviesRequestResults {
        val movieResults = apiService.getMoviesList(Constants.MOVIE_CATEGORY_UPCOMING, 1)
        movieResults.results.forEach { movie ->
            Log.e("MoviesRepository", "fetchMoviesResponse: ${movie}")}
        return movieResults
    }
}