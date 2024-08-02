package com.example.movietestapp.data.api

import com.example.movietestapp.data.dto.results.MoviesRequestResults
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilterMovieApi {
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String?,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("page") page: Int,
        @Query("year") year: Int?,
        @Query("with_genres") withGenres: String?,
        @Query("with_keywords") withKeywords: String?
    ): MoviesRequestResults
}