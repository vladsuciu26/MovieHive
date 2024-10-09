package com.example.movietestapp.data.api

import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.data.dto.details.DetailsData
import com.example.movietestapp.data.dto.results.MovieVideosResponse
import com.example.movietestapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailApi {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ) : DetailsData

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieVideosResponse
}