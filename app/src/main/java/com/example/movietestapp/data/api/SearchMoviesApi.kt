package com.example.movietestapp.data.api

import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMoviesApi {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): MoviesRequestResults
}