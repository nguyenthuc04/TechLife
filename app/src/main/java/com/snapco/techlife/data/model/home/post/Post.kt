package com.snapco.techlife.data.model.home.post

import kotlinx.serialization.Serializable

@Serializable
data class PostTextParams(
    val caption: String,
    val userId: String
)

@Serializable
data class Post(
    val postId: String,
    val caption: String,
    val imageUrl: String,
    val createdAt: String,
    val likesCount: String,
    val commentsCount: String,
    val userId: String,
    val userName: String,
    val userImageUrl: String?,
    val isLiked: Boolean,
    val isOwnPost: Boolean
)

// Response classes for different operations with Post

@Serializable
data class GetPostResponse(
    val success: Boolean,
    val post: Post?,
    val message: String? = null
)

@Serializable
data class CreatePostResponse(
    val success: Boolean,
    val post: Post?,
    val message: String? = null
)

@Serializable
data class UpdatePostResponse(
    val success: Boolean,
    val post: Post?,
    val message: String? = null
)

@Serializable
data class DeletePostResponse(
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class PostsListResponse(
    val success: Boolean,
    val posts: List<Post> = emptyList(),
    val message: String? = null
)
