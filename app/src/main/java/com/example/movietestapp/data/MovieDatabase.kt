package com.example.movietestapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movietestapp.data.dao.MovieDao
import com.example.movietestapp.data.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}