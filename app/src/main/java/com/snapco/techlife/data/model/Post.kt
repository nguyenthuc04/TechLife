package com.snapco.techlife.data.model

data class Post(
    val _id: String,
    val userId: String,
    val userName: String,
    val userImageUrl: String?,
    val caption: String,
    val imageUrl: List<String>?,
    var likesCount: Int = 0, // Changed to Int
    var commentsCount: Int = 0, // Changed to Int
    val createdAt: String,
    var isLiked: Boolean = false, // Ensure this exists if you're using it
    var likes: List<String>?,
    var comments: List<Comment>?,
)

data class AddCommentRequest(
    val userId: String,
    val userName: String,
    val userImageUrl: String,
    val text: String,
)

data class AddCommentResponse(
    val success: Boolean,
    val message: String,
    val post: Post?,
)

data class GetCommentResponse(
    val success: Boolean,
    val comments: List<Comment>?,
)

data class Comment(
    val _id: String,
    val postId: String?,
    val userId: String,
    val userName: String,
    val userImageUrl: String?,
    val text: String,
    val createdAt: String,
)

data class LikeResponse(
    val success: Boolean,
    val message: String,
    val post: Post,
)

data class GetPostResponse(
    val success: Boolean,
    val post: Post?,
    val message: String? = null,
)

data class CreatePostRequest(
    val userId: String,
    val caption: String,
    val imageUrl: List<String>?,
    val userName: String,
    val userImageUrl: String?,
)

data class CreatePostResponse(
    val success: Boolean,
    val message: String? = null,
    val post: Post?,
)

data class UpdatePostResponse(
    val success: Boolean,
    val post: Post?,
    val message: String? = null,
)

data class DeletePostResponse(
    val success: Boolean,
    val message: String? = null,
)

data class PostsListResponse(
    val success: Boolean,
    val posts: List<Post> = emptyList(),
    val message: String? = null,
)

data class Like(
    val likeId: String,
    val postId: String,
    val userId: String,
    val likedAt: String,
)

data class PostProfileResponse(
    val success: Boolean,
    val posts: List<Post>?,
)
