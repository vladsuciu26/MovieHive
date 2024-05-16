package com.example.movietestapp.data.api

import com.example.movietestapp.data.dto.results.UpcomingMoviesRequestResults
import com.example.movietestapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UpcomingMoviesApi {
    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ) : UpcomingMoviesRequestResults
}