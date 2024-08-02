package com.example.movietestapp.ui.review.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietestapp.data.dto.ReplyData
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.data.repositories.ReviewsRepository
import com.example.movietestapp.ui.review.state.ReviewState
import com.example.movietestapp.ui.review.state.ReviewStateWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {

    private val reviewsRepository: ReviewsRepository = ReviewsRepository()
    private val _uiReviewState = MutableStateFlow(ReviewStateWrapper(null))
    val uiReviewState: StateFlow<ReviewStateWrapper> = _uiReviewState

    fun addReview(content: String, userId: String, username: String, movieId: Int) {
        val newReview = ReviewData(
            id = userId,
            userId = userId,
            username = username,
            content = content,
            movieId = movieId
        )
        reviewsRepository.saveReview(newReview)
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

    fun updateReview(review: ReviewData, newContent: String) {
        reviewsRepository.updateReview(review, newContent)
        loadReviewsByMovieId(review.movieId)
    }

    fun deleteReview(review: ReviewData) {
        reviewsRepository.deleteReview(review)
        loadReviewsByMovieId(review.movieId)
    }
}