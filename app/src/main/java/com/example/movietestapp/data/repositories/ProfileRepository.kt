package com.example.movietestapp.data.repositories

import android.util.Log
import com.example.movietestapp.data.api.MovieDetailApi
import com.example.movietestapp.data.dto.ReplyData
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.data.dto.UserData
import com.example.movietestapp.data.retrofit.RetrofitHelper
import com.example.movietestapp.ui.profile.states.ProfileState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val database = FirebaseDatabase.getInstance()
    private val reviewsRef = database.getReference("reviews")

    private var profile = ProfileState(
        userId = "-O3htTNgZFexLPHvgtXG",
        name = "Pablo",
        memberSince = "2 years",
        note = "Etiam id dolor ex. Vivamus lobortis varius tortor, the elementum eleifend ligula tincidunt eget. Mauris ut semper odio. Etiam at justo a massa.\n\nEtiam id dolor ex. Vivamus lobortis varius tortor, the elementum eleifend ligula tincidunt eget. Mauris ut.",
        interests = "User interests and hobbies.",
        contactEmail = "pablo@yahoo.com",
        contactPhone = "+1234567890"
    )

//    fun getProfile(): ProfileState {
//        Log.d("ProfileRepository", "getProfile called")
//        Log.d("ProfileRepository", "Profile userId: ${profile.userId}")
//        return profile
//    }

    suspend fun getProfile(): ProfileState {
        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.d("ProfileRepository", "Fetching profile for current user")

        if (currentUser != null) {
            val userId = currentUser.uid
            Log.d("ProfileRepository", "Current user ID: $userId")

            try {
                val userSnapshot = database.getReference("users").child(userId).get().await()
                Log.d("ProfileRepository", "User snapshot retrieved: ${userSnapshot.value}")

                val userData = userSnapshot.getValue(UserData::class.java)

                if (userData != null) {
                    Log.d("ProfileRepository", "User data retrieved: $userData")
                    profile = ProfileState(
                        userId = userData.id ?: "Unknown ID",
                        name = userData.username ?: "Unknown",
                        memberSince = "2 years",
                        note = profile.note,
                        interests = profile.interests,
                        contactEmail = userData.email ?: "No email",
                        contactPhone = profile.contactPhone
                    )
                    Log.d("ProfileRepository", "Profile state updated: $profile")
                } else {
                    Log.e("ProfileRepository", "User data is null for userId: $userId")
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Error retrieving user data from Firebase", e)
            }
        } else {
            Log.w("ProfileRepository", "No user is currently logged in.")
        }

        return profile
    }



    suspend fun getUserReviews(userId: String): List<Pair<ReviewData, String>> {
        Log.d("ProfileRepository", "getUserReviews called with userId: $userId")
        return try {
            val snapshot = reviewsRef.orderByChild("userId").equalTo(userId).get().await()
            val reviews = mutableListOf<Pair<ReviewData, String>>()
            for (data in snapshot.children) {
                val review = data.getValue(ReviewData::class.java)
                if (review != null) {
                    val movieTitle = getMovieTitleById(review.movieId)
                    reviews.add(Pair(review, movieTitle))
                }
            }
            Log.d("ProfileRepository", "Retrieved ${reviews.size} reviews")
            reviews
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error getting reviews", e)
            emptyList()
        }
    }

    private suspend fun getMovieTitleById(movieId: Int): String {
        return try {
            val movieDetailApi = RetrofitHelper.buildService(MovieDetailApi::class.java)
            val movieDetails = movieDetailApi.getMovieDetail(movieId)
            movieDetails.title ?: "Unknown Title"
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error getting movie title", e)
            "Unknown Title"
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

