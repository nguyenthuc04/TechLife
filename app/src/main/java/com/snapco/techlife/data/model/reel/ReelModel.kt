package com.snapco.techlife.data.model.reel

import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class ReelTextParams(
    val caption: String,
    val userId: String
)

@Serializable
data class Reel(
    var reelId: String,
    var caption: String,
    var videoUrl: String,
    var createdAt: Timestamp = Timestamp.now(),
    var likesCount: String,
    var commentsCount: String,
    var userId: String,
    var userName: String,
    var userImageUrl: String?,
    var isLiked: Boolean,
    var isOwnPost: Boolean
)

@Serializable
data class GetReelResponse(
    val success: Boolean,
    val reel: Reel?,
    val message: String? = null
)

@Serializable
data class CreateReelResponse(
    val success: Boolean,
    val reel: Reel?,
    val message: String? = null
)

@Serializable
data class UpdateReelResponse(
    val success: Boolean,
    val reel: Reel?,
    val message: String? = null
)

@Serializable
data class DeleteReelResponse(
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class ReelsListResponse(
    val success: Boolean,
    val reels: List<Reel> = emptyList(),
    val message: String? = null
)
