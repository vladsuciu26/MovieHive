package com.example.movietestapp.data.repositories

import android.util.Log
import com.example.movietestapp.data.dto.ReplyData
import com.example.movietestapp.data.dto.ReviewData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

class ReviewsRepository {

    private val database = FirebaseDatabase.getInstance().getReference("reviews")

    fun saveReview(review: ReviewData) {
        val reviewId = database.push().key ?: review.id
        review.id = reviewId // Ensure the review ID is set
        database.child(reviewId).setValue(review)
            .addOnSuccessListener {
                Log.d("ReviewsRepository", "Review saved successfully: $review")
            }
            .addOnFailureListener { e ->
                Log.e("ReviewsRepository", "Failed to save review", e)
            }
    }

    fun saveReply(reviewId: String, reply: ReplyData) {
        val replyId = database.child(reviewId).child("replies").push().key ?: return
        reply.replyId = replyId
        database.child(reviewId).child("replies").child(replyId).setValue(reply)
            .addOnSuccessListener {
                Log.d("ReviewsRepository", "Reply saved successfully: $reply")
            }
            .addOnFailureListener { e ->
                Log.e("ReviewsRepository", "Failed to save reply", e)
            }
    }

    fun getReviewsByMovieId(movieId: Int): Flow<List<ReviewData>> {
        val reviewsFlow = MutableStateFlow<List<ReviewData>>(emptyList())

        database.orderByChild("movieId").equalTo(movieId.toDouble()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviewsList = mutableListOf<ReviewData>()
                for (reviewSnapshot in snapshot.children) {
                    val review = reviewSnapshot.getValue(ReviewData::class.java)
                    if (review != null) {
                        reviewsList.add(review)
                    }
                }
                reviewsFlow.value = reviewsList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("ReviewsRepository", "Failed to load reviews", error.toException())
            }
        })

        return reviewsFlow
    }

    fun updateReview(review: ReviewData) {
        val updates = mapOf(
            "content" to review.content,
            "rating" to review.rating
        )
        database.child(review.id).updateChildren(updates)
            .addOnSuccessListener {
                Log.d("ReviewsRepository", "Review updated successfully: $review")
            }
            .addOnFailureListener { e ->
                Log.e("ReviewsRepository", "Failed to update review", e)
            }
    }

    fun deleteReview(review: ReviewData) {
        database.child(review.id).removeValue()
            .addOnSuccessListener {
                Log.d("ReviewsRepository", "Review deleted successfully: $review")
            }
            .addOnFailureListener { e ->
                Log.e("ReviewsRepository", "Failed to delete review", e)
            }
    }
}
