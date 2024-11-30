package com.snapco.techlife.data.model

class Reels

data class Reel(
    val _id: String,
    val userId: String,
    val userName: String,
    val userImageUrl: String?,
    val caption: String,
    val videoUrl: List<String>?,
    var likesCount: Int = 0,
    var commentsCount: Int = 0,
    val createdAt: String,
    var isLiked: Boolean = false,
    var likes: List<String>?,
    var comments: List<CommentReel>?,
)

data class CommentReel(
    val _id: String,
    val reelId: String?,
    val userId: String,
    val userName: String,
    val userImageUrl: String?,
    val text: String,
    val createdAt: String,
)

data class CreateReelRequest(
    val userId: String,
    val caption: String,
    val videoUrl: List<String>?,
    val userName: String,
    val userImageUrl: String?,
)

data class CreateReelResponse(
    val success: Boolean,
    val message: String? = null,
    val reel: Reel?,
)

data class AddCommentReelRequest(
    val userId: String,
    val userName: String,
    val userImageUrl: String,
    val text: String,
    val yourID: String,
)

data class AddCommentReelResponse(
    val success: Boolean,
    val message: String,
    val reel: Reel?,
)

data class GetCommentReelResponse(
    val success: Boolean,
    val comments: List<CommentReel>?,
)

data class LikeReelResponse(
    val success: Boolean,
    val message: String,
    val reel: Reel,
)

data class ReelProfileResponse(
    val success: Boolean,
    val reels: List<Reel>?,
)
