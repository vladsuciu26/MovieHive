package com.example.movietestapp.data.api

import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailApi {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ) : MovieData
}