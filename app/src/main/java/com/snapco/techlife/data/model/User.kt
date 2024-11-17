package com.snapco.techlife.data.model

data class User(
    val id: String = "",
    val account: String = "",
    val password: String = "",
    val birthday: String = "",
    val name: String = "",
    val nickname: String = "",
    val avatar: String = "",
    val following: List<String> = emptyList(),
    val followers: List<String> = emptyList(),
    val bio: String = "",
    val posts: List<String> = emptyList(),
    val accountType: String = "",
    val streamToken: String? = null // thuoc tinh token cho chat
)

data class GetUserResponse(
    val user: User,
    val followingCount: Int,
    val followersCount: Int,
    val postsCount: Int,
    val streamToken: String? = null // thuoc tinh token cho chat
)

data class LoginRequest(
    val account: String,
    val password: String,
)

data class LoginResponse(
    val message: String,
    val token: String,
    val user: User,
    val streamToken: String? = null,
    val apiKey: String? = null
)

data class UpdateUserRequest(
    val account: String,
    val password: String,
    val birthday: String,
    val name: String,
    val nickname: String,
    val bio: String,
    val avatar: String,
    val accountType: String,
    val streamToken: String? = null // thuoc tinh token cho chat
)

data class UpdateUserResponse(
    val message: String,
    val user: User,
    val streamToken: String? = null // thuoc tinh token cho chat
)

data class CreateUserRequest(
    val account: String = "",
    val password: String = "",
    val birthday: String = "",
    val name: String = "",
    val nickname: String = "",
    val bio: String = "",
    val avatar: String = "",
    val accountType: String = "",
    val streamToken: String? = null // thuoc tinh token cho chat
)

data class CreateUserResponse(
    val message: String,
    val user: User,
    val streamToken: String? = null
)

data class CheckEmailRequest(
    val account: String,
)

data class CheckEmailResponse(
    val exists: Boolean,
    val message: String,
    val account: String,
)

data class SearchUserResponse(
    val _id: String = "",
    val account: String = "",
    val password: String = "",
    val birthday: String = "",
    val name: String = "",
    val nickname: String = "",
    val avatar: String = "",
    val following: List<String> = emptyList(),
    val followers: List<Any> = emptyList(),
    val bio: String = "",
    val posts: List<String> = emptyList(),
    val accountType: String = "",
)

data class FollowRequest(
    val followerId: String,
    val followeeId: String
)

data class FollowResponse(
    val message: String,
    val success: Boolean,
    val updatedUser: User
)

data class UnfollowRequest(
    val followerId: String,
    val followeeId: String
)

data class UnfollowResponse(
    val message: String,
    val success: Boolean,
    val updatedUser: User
)

data class TokenStreamRequest(
    val userId: String
)

data class TokenStreamResponse(
    val streamToken: String,
)