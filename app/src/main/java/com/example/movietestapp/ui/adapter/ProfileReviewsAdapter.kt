package com.example.movietestapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.databinding.ReviewProfileItemBinding

class ProfileReviewsAdapter(
    private val reviews: List<Pair<ReviewData, String>>,
    private val onItemClick: (ReviewData) -> Unit
) : RecyclerView.Adapter<ProfileReviewsAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ReviewProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reviewPair: Pair<ReviewData, String>) {
            val review = reviewPair.first
            val movieTitle = reviewPair.second
            binding.reviewText.text = "You left a review on the movie: $movieTitle"
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