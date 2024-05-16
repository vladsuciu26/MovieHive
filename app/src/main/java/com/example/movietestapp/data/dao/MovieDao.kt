package com.example.movietestapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.movietestapp.data.entity.MovieEntity

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMovieByCategory(category: String): List<MovieEntity>
}