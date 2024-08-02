package com.example.movietestapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.databinding.ReviewListItemBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.PopupMenu
import android.widget.TextView
import com.example.movietestapp.R
import com.example.movietestapp.databinding.ItemReplyBinding

class ReviewsAdapter(
    private var reviewsList: ArrayList<ReviewData>,
    private val currentUserId: String?,
    private val onEditClick: (ReviewData) -> Unit,
    private val onDeleteClick: (ReviewData) -> Unit,
    private val onReplyClick: (ReviewData) -> Unit
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(val binding: ReviewListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ReviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewsList[position]
        holder.binding.reviewUsername.text = review.username
        holder.binding.reviewContent.text = review.content

        Log.d("ReviewsAdapter", "Current User ID: $currentUserId, Review User ID: ${review.userId}")

        if (review.userId == currentUserId) {
            holder.binding.reviewButtonsLayout.visibility = View.VISIBLE
            holder.binding.replyReviewButton.visibility = View.GONE
        } else {
            holder.binding.reviewButtonsLayout.visibility = View.GONE
            holder.binding.replyReviewButton.visibility = View.VISIBLE
        }

        holder.binding.editReviewButton.setOnClickListener {
            onEditClick(review)
        }

        holder.binding.deleteReviewButton.setOnClickListener {
            onDeleteClick(review)
        }

        holder.binding.replyReviewButton.setOnClickListener {
            onReplyClick(review)

        }

        holder.binding.repliesLayout.removeAllViews() // Clear existing views
        review.replies?.forEach { (_, reply) ->
            val replyBinding = ItemReplyBinding.inflate(LayoutInflater.from(holder.binding.root.context),
                holder.binding.repliesLayout, false)
            replyBinding.replyUsername.text = reply.replyUsername
            replyBinding.replyContent.text = reply.replyContent
            holder.binding.repliesLayout.addView(replyBinding.root)
        }

        holder.binding.repliesLayout.visibility = if (review.replies != null) View.VISIBLE else View.GONE

    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }

    fun updateReviews(newReviews: ArrayList<ReviewData>) {
        reviewsList = newReviews
        notifyDataSetChanged()
    }

}
