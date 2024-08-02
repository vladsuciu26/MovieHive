package com.example.movietestapp.data.repositories

import android.util.Log
import com.example.movietestapp.data.api.FavoriteMovieApi
import com.example.movietestapp.data.dto.results.FavoriteMovieRequestResults
import com.example.movietestapp.data.dto.results.FavoriteResponse
import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.data.retrofit.RetrofitHelper
import com.example.movietestapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class FavoritesRepository {
    private val apiService = RetrofitHelper.buildService(FavoriteMovieApi::class.java)

    suspend fun addToFavorites(accountId: Int, sessionId: String, movieId: Int): FavoriteResponse {
        val favoriteRequest = FavoriteMovieRequestResults("movie", movieId, true)
        Log.d("FavoritesRepository", "Sending request to add favorite: $favoriteRequest")
        val response = apiService.addFavoriteMovie(accountId, sessionId, favoriteRequest)
        Log.d("FavoritesRepository", "Received response: $response")
        return response
    }

    suspend fun getFavoriteMovies(accountId: Int, sessionId: String): MoviesRequestResults {
        return apiService.getFavoriteMovies(Constants.ACCOUNT_ID, Constants.SESSION_ID, Constants.API_KEY)
    }
}