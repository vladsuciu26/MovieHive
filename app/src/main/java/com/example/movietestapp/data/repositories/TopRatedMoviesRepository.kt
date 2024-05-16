package com.example.movietestapp.data.repositories

import android.util.Log
import com.example.movietestapp.data.api.TopRatedMoviesApi
import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.data.retrofit.RetrofitHelper
import com.example.movietestapp.util.Constants

class TopRatedMoviesRepository {
    private val apiService = RetrofitHelper.buildService(TopRatedMoviesApi::class.java)

    suspend fun fetchTopRatedMoviesResponse() : MoviesRequestResults {
        val movieResults = apiService.getMoviesList(Constants.MOVIE_CATEGORY_TOP_RATED, 1)
        movieResults.results.forEach { movie ->
            Log.e("MoviesRepository", "fetchMoviesResponse: ${movie}")}
        return movieResults
    }
}