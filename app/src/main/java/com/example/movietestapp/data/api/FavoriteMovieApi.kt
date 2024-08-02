package com.example.movietestapp.data.api

import com.example.movietestapp.data.dto.results.FavoriteMovieRequestResults
import com.example.movietestapp.data.dto.results.FavoriteResponse
import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.util.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.GET

interface FavoriteMovieApi {
    @POST("account/{account_id}/favorite")
    suspend fun addFavoriteMovie(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Body favoriteRequest: FavoriteMovieRequestResults
    ): FavoriteResponse

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): MoviesRequestResults
}