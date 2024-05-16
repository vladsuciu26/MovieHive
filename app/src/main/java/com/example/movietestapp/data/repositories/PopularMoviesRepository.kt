package com.example.movietestapp.data.repositories

import android.util.Log
import com.example.movietestapp.data.api.PopularMoviesApi
import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.data.retrofit.RetrofitHelper
import com.example.movietestapp.util.Constants

class PopularMoviesRepository {
    private val apiService = RetrofitHelper.buildService(PopularMoviesApi::class.java)

    suspend fun fetchPopularMoviesResponse(): MoviesRequestResults {
        val movieResults = apiService.getMoviesList(Constants.MOVIE_CATEGORY_POPULAR, 1)
       movieResults.results.forEach { movie ->
            Log.e("MoviesRepository", "fetchMoviesResponse: ${movie}")}
        return movieResults
    }
}
