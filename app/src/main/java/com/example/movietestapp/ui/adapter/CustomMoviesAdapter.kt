package com.example.movietestapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movietestapp.R
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.databinding.SimilarMovieListItemBinding
import com.example.movietestapp.ui.Navigator
import com.example.movietestapp.util.Constants

class CustomMoviesAdapter(
    private val customMovies: List<MovieData>
) : RecyclerView.Adapter<CustomMoviesAdapter.CustomMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomMoviesAdapter.CustomMovieViewHolder =
        CustomMovieViewHolder(
            SimilarMovieListItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.similar_movie_list_item, parent, false)
            )
        ).also { Log.e("SimilarMoviesAdapter", "") }

    override fun getItemCount(): Int = customMovies.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: CustomMovieViewHolder, position: Int) {
        holder.bind(customMovies[position])
    }

    inner class CustomMovieViewHolder(private val binding: SimilarMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val similarMovieTitleTextView: TextView
            get() = binding.similarMovieTitle

        private val similarMoviePosterImageView: ImageView
            get() = binding.similarMoviePoster

        private val similarMovieRatingTextView: TextView
            get() = binding.similarMovieRating

        fun bind(item: MovieData) {
            similarMovieTitleTextView.text = item.title
            similarMovieRatingTextView.text = item.voteAverage.toString()
            val imageUrl = "${Constants.IMAGE_BASE_URL}${item.posterPath}"
            Glide.with(similarMoviePosterImageView.context)
                .load(imageUrl)
                .into(similarMoviePosterImageView)

            binding.root.setOnClickListener {
                Navigator.getInstance().openMovieDetailFragment(item.id!!)
            }
        }

    }
}