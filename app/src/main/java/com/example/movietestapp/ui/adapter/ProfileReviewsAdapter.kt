package com.example.movietestapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.databinding.ReviewProfileItemBinding

class ProfileReviewsAdapter(
    private val reviews: List<ReviewData>,
    private val onItemClick: (ReviewData) -> Unit
) : RecyclerView.Adapter<ProfileReviewsAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ReviewProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewData) {
            binding.reviewText.text = "You left a review on the movie with the id: ${review.movieId}"
            binding.root.setOnClickListener {
                onItemClick(review)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ReviewProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount() = reviews.size
}
