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

internal class FavoriteMoviesAdapter (
    private var itemsList: ArrayList<MovieData>
) : RecyclerView.Adapter<FavoriteMoviesAdapter.MoviesViewHolder>() {

    init {
        Log.d("FavoriteMoviesAdapter", "itemsList is ${if (itemsList.isEmpty()) "empty" else "not empty"}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder =
        MoviesViewHolder(
            MovieListItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_list_item, parent, false)
            )
        ).also { Log.e("FavoriteMoviesAdapter", "") }

    override fun getItemCount(): Int = itemsList.size

    override fun getItemViewType(position: Int): Int {
        Log.e("FavoriteMoviesAdapter", "getItemViewType")
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        Log.e("FavoriteMoviesAdapter", "onBindViewHolder")
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
            categoryTextView.text = item.genreIds.toString()
            releaseDateTextView.text = item.releaseDate
            ratingBarTextView.rating = item.voteAverage?.toFloat()?.div(2) ?: 0.0f
            val imageUrl = "${Constants.IMAGE_BASE_URL}${item.posterPath}"
            Log.e("FavoriteMoviesAdapter", "imageUrl: $imageUrl")
            Glide.with(moviePosterImageView.context)
                .load(imageUrl)
                .into(moviePosterImageView)

            itemView.setOnClickListener {
                Navigator.getInstance().openMovieDetailFragment(item.id!!)
            }
        }
    }

    fun updateData(newItemsList: List<MovieData>) {
        itemsList.clear()
        itemsList.addAll(newItemsList)
        notifyDataSetChanged()
    }
}