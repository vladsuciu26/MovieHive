package com.example.movietestapp.data.api

import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimilarMoviesApi {
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ) : MoviesRequestResults
}