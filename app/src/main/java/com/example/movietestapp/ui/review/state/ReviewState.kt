package com.example.movietestapp.ui.review.state

import com.example.movietestapp.data.dto.ReviewData

data class ReviewState(
    val reviews: ArrayList<ReviewData>
)

data class ReviewStateWrapper(
    val reviewState: ReviewState?
)