package com.example.movietestapp.data.dto

data class ReviewData (
    var id: String = "",
    val userId: String = "",
    val username: String = "",
    var content: String = "",
    val movieId: Int = 0,
    val replies: Map<String, ReplyData>? = null
)

data class ReplyData (
    var replyId: String = "",
    val replyContent: String = "",
    val replyUserId: String = "",
    val replyUsername: String = "",
    val movieId: Int = 0
)