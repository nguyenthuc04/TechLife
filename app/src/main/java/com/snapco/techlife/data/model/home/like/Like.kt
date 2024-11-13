package com.snapco.techlife.data.model.home.like

data class Like(
    val likeId: String,
    val postId: String,
    val userId: String,
    val likedAt: String // Timestamp for when the like was made
)