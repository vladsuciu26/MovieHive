package com.example.movietestapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movietestapp.R
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.databinding.MovieListItemBinding
import com.example.movietestapp.ui.Navigator
import com.example.movietestapp.util.Constants

internal class HomeMoviesAdapter (
    private var itemsList: ArrayList<MovieData>
) : RecyclerView.Adapter<HomeMoviesAdapter.MoviesViewHolder>() {

    private val genresMap = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "War",
        37 to "Western"
    )

    init {
        Log.d("HomeMoviesAdapter", "itemsList is ${if (itemsList.isEmpty()) "empty" else "not empty"}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder =
        MoviesViewHolder(
            MovieListItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_list_item, parent, false)
            )
        ).also { Log.e("HomeMoviesAdapter", "") }

    override fun getItemCount(): Int = itemsList.size

    override fun getItemViewType(position: Int): Int {
        Log.e("HomeMoviesAdapter", "getItemViewType")
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        Log.e("HomeMoviesAdapter", "onBindViewHolder")
        val movie = itemsList[position]
        holder.bind(movie)
    }

    inner class MoviesViewHolder(private var binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val movieTitleTextView: TextView
            get() = binding.movieTitle
        private val categoryTextView: TextView
            get() = binding.movieCategory
        private val releaseDateTextView: TextView
            get() = binding.movieReleaseDate
        private val ratingBarTextView: RatingBar
            get() = binding.movieRatingBar
        private val moviePosterImageView: ImageView
            get() = binding.moviePoster


        fun bind(item: MovieData) {
            movieTitleTextView.text = item.title
            val genres = item.genreIds.mapNotNull { genresMap[it] }.take(4).joinToString(", ")
            categoryTextView.text = genres
            releaseDateTextView.text = item.releaseDate
            ratingBarTextView.rating = item.voteAverage?.toFloat()?.div(2) ?: 0.0f
            val imageUrl = "${Constants.IMAGE_BASE_URL}${item.posterPath}"
            Log.e("HomeMoviesAdapter", "imageUrl: $imageUrl")
            Glide.with(moviePosterImageView.context)
                .load(imageUrl)
                .into(moviePosterImageView)

            itemView.setOnClickListener {
                Navigator.getInstance().openMovieDetailFragment(item.id!!)
            }
        }
    }
}
