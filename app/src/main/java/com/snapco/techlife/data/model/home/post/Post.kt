package com.snapco.techlife.data.model.home.post

import com.snapco.techlife.data.model.home.comment.Comment
import com.snapco.techlife.data.model.home.like.Like
import kotlinx.serialization.Serializable

@Serializable
data class PostTextParams(
    val caption: String,
    val userId: String
)

@Serializable
data class Post(
    val postId: String,
    val userId: String,
    val userName: String,
    val userImageUrl: String?,
    val caption: String,
    val imageUrl: String?,
    var likesCount: Int = 0, // Changed to Int
    var commentsCount: Int = 0, // Changed to Int
    val createdAt: String,
    var isLiked: Boolean = false, // Ensure this exists if you're using it
    var likes: List<Like> = listOf(),
    var comments: List<Comment> = listOf()
)

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
