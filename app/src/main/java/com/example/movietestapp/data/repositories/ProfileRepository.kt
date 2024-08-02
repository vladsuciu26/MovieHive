package com.example.movietestapp.data.repositories

import android.util.Log
import com.example.movietestapp.data.dto.ReplyData
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.ui.profile.states.ProfileState
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val database = FirebaseDatabase.getInstance()
    private val reviewsRef = database.getReference("reviews")

    private var profile = ProfileState(
        userId = "-NuT_ffE6c6DLu6xFI-l",  // Exemplu de userId
        name = "Pablo Escobar",
        memberSince = "2 years",
        followers = "10.6K",
        following = "1.2K",
        note = "Etiam id dolor ex. Vivamus lobortis varius tortor, the elementum eleifend ligula tincidunt eget. Mauris ut semper odio. Etiam at justo a massa.\n\nEtiam id dolor ex. Vivamus lobortis varius tortor, the elementum eleifend ligula tincidunt eget. Mauris ut.",
        interests = "User interests and hobbies.",
        contactEmail = "pablo.escobar@example.com",
        contactPhone = "+1234567890"
    )

    fun getProfile(): ProfileState {
        Log.d("ProfileRepository", "getProfile called")
        Log.d("ProfileRepository", "Profile userId: ${profile.userId}")
        return profile
    }

    suspend fun getUserReviews(userId: String): List<ReviewData> {
        Log.d("ProfileRepository", "getUserReviews called with userId: $userId")
        return try {
            val snapshot = reviewsRef.orderByChild("userId").equalTo(userId).get().await()
            val reviews = mutableListOf<ReviewData>()
            for (data in snapshot.children) {
                val review = data.getValue(ReviewData::class.java)
                if (review != null) {
                    reviews.add(review)
                }
            }
            Log.d("ProfileRepository", "Retrieved ${reviews.size} reviews")
            reviews
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error getting reviews", e)
            emptyList()
        }
    }

    suspend fun getUserReplies(userId: String): List<ReplyData> {
        Log.d("ProfileRepository", "getUserReplies called with userId: $userId")
        return try {
            val snapshot = reviewsRef.orderByChild("replies/replyUserId").equalTo(userId).get().await()
            Log.d("ProfileRepository", "Snapshot for replies: $snapshot")
            val replies = mutableListOf<ReplyData>()
            for (data in snapshot.children) {
                Log.d("ProfileRepository", "Processing review: ${data.key}")
                val reviewReplies = data.child("replies").children
                for (replySnapshot in reviewReplies) {
                    Log.d("ProfileRepository", "Processing reply: ${replySnapshot.key}")
                    val reply = replySnapshot.getValue(ReplyData::class.java)
                    if (reply != null && reply.replyUserId == userId) {
                        Log.d("ProfileRepository", "Adding reply: $reply")
                        replies.add(reply)
                    }
                }
            }
            Log.d("ProfileRepository", "Retrieved ${replies.size} replies")
            replies
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error getting replies", e)
            emptyList()
        }
    }

    fun updateProfile(updatedProfile: ProfileState) {
        profile = updatedProfile
    }
}
