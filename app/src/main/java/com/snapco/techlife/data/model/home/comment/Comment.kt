package com.snapco.techlife.data.model.home.comment

data class Comment(
    val commentId: String,
    val postId: String,
    val userId: String,
    val userName: String,
    val userImageUrl: String?,
    val commentText: String,
    val commentedAt: String // Timestamp for when the comment was made
)