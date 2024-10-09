package com.example.movietestapp.ui.review.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietestapp.data.dto.ReplyData
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.data.repositories.ReviewsRepository
import com.example.movietestapp.ui.profile.viewmodel.ProfileViewModel
import com.example.movietestapp.ui.review.state.ReviewState
import com.example.movietestapp.ui.review.state.ReviewStateWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {

    private val reviewsRepository: ReviewsRepository = ReviewsRepository()
    private val _uiReviewState = MutableStateFlow(ReviewStateWrapper(null))
    val uiReviewState: StateFlow<ReviewStateWrapper> = _uiReviewState
    val profileViewModel = ProfileViewModel();

//    fun addReview(content: String, userId: String, username: String, movieId: Int, rating: Int) {
//        val newReview = ReviewData(
//            id = userId,
//            userId = userId,
//            username = username,
//            content = content,
//            movieId = movieId,
//            rating = rating
//        )
//        reviewsRepository.saveReview(newReview)
//        loadReviewsByMovieId(movieId)
//    }

    init {
        profileViewModel.loadProfile()
    }

    fun addReview(content: String, movieId: Int, rating: Int) {
        Log.d("ReviewViewModel", "Attempting to add review")

        val userProfile = profileViewModel.uiProfileState.value?.profileState
        Log.d("ReviewViewModel", "UserProfile: $userProfile")

        val userId = userProfile?.userId ?: run {
            Log.e("ReviewViewModel", "User ID is null, cannot add review.")
            return
        }
        val username = userProfile.name ?: "Unknown"
        Log.d("ReviewViewModel", "Adding review with userId: $userId, username: $username")

        val newReview = ReviewData(
            id = userId,
            userId = userId,
            username = username,
            content = content,
            movieId = movieId,
            rating = rating
        )

        reviewsRepository.saveReview(newReview)
        Log.d("ReviewViewModel", "Review saved successfully, reloading reviews for movieId: $movieId")
        loadReviewsByMovieId(movieId)
    }

    fun loadReviewsByMovieId(movieId: Int) {
        viewModelScope.launch {
            reviewsRepository.getReviewsByMovieId(movieId).collect { reviews ->
                _uiReviewState.value = ReviewStateWrapper(ReviewState(ArrayList(reviews)))
            }
        }
    }

    fun addReply(review: ReviewData, replyContent: String, replyUsername: String, movieId: Int, userId: String) {
        val replyData = ReplyData(
            replyContent = replyContent,
            replyUsername = replyUsername,
            movieId = movieId,
            replyUserId = userId
        )
        reviewsRepository.saveReply(review.id, replyData)
        loadReviewsByMovieId(review.movieId)
    }

    fun updateReview(review: ReviewData, newContent: String, newRating: Int) {
        review.content = newContent
        review.rating = newRating
        reviewsRepository.updateReview(review)
        loadReviewsByMovieId(review.movieId)
    }

    fun deleteReview(review: ReviewData) {
        reviewsRepository.deleteReview(review)
        loadReviewsByMovieId(review.movieId)
    }
}