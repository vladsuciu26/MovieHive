package com.example.movietestapp.data.repositories

import com.example.movietestapp.data.api.FilterMovieApi
import com.example.movietestapp.data.dto.results.MoviesRequestResults
import com.example.movietestapp.data.retrofit.RetrofitHelper
import com.example.movietestapp.util.Constants

class FilterMovieRepository {

    private val apiService = RetrofitHelper.buildService(FilterMovieApi::class.java)

    suspend fun fetchFilteredMovies(year: Int?, sortBy: String?, genres: String?, keywords: String?, page: Int): MoviesRequestResults {
        return apiService.discoverMovies(
            apiKey = Constants.API_KEY,
            year = year,
            sortBy = sortBy,
            withGenres = genres,
            withKeywords = keywords,
            page = page
        )
    }
}