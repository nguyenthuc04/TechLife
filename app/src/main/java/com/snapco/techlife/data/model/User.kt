package com.snapco.techlife.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

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
    val streamToken: String? = null, // thuoc tinh token cho chat
)

data class CreateReviewRequest(
    val idMentor: String,
    val rating: Int,
    val comment: String,
    val userId: String,
)

data class CreateReviewResponse(
    val success: Boolean,
    val message: String,
)

data class GetReviewResponse(
    val success: Boolean,
    val reviews: List<Review>,
)

data class GetAverageRatingResponse(
    val success: Boolean,
    val averageRating: Int,
)

data class CheckUserInAnyCourseResponse(
    val success: Boolean,
    val isCheck: Boolean,
)

class Review(
    @SerializedName("_id") val id: String?,
    val rating: Int,
    val comment: String,
    val userId: UserReview,
    val date: String,
)

data class UserReview(
    val _id: String,
    val name: String,
    val avatar: String,
)

data class PremiumRequest(
    val userId: String,
    val userName: String,
    val userImageUrl: String?,
    val imageUrl: String,
)

data class CreatePremiumResponse(
    val success: Boolean,
    val message: String? = null,
    val premium: PremiumRequest?,
)

data class GetUserResponse(
    val user: User,
    val followingCount: Int,
    val followersCount: Int,
    val postsCount: Int,
    val streamToken: String? = null, // thuoc tinh token cho chat
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
    val apiKey: String? = null,
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
    val streamToken: String? = null, // thuoc tinh token cho chat
)

data class UpdateUserResponse(
    val message: String,
    val user: User,
    val streamToken: String? = null, // thuoc tinh token cho chat
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
    val streamToken: String? = null, // thuoc tinh token cho chat
)

data class CreateUserResponse(
    val message: String,
    val user: User,
    val streamToken: String? = null,
)

data class CheckEmailRequest(
    val account: String,
)

data class CheckEmailResponse(
    val exists: Boolean,
    val message: String,
    val account: String,
)

data class UserAccount(
    val id: String,
    val account: String,
    val password: String?,
    val avatar: String,
    val name: String,
    val state: String,
    val status: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun writeToParcel(
        parcel: Parcel,
        flags: Int,
    ) {
        parcel.writeString(id)
        parcel.writeString(account)
        parcel.writeString(password)
        parcel.writeString(avatar)
        parcel.writeString(name)
        parcel.writeString(state)
        parcel.writeString(status)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<UserAccount> {
        override fun createFromParcel(parcel: Parcel): UserAccount = UserAccount(parcel)

        override fun newArray(size: Int): Array<UserAccount?> = arrayOfNulls(size)
    }
}

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
    val followeeId: String,
)

data class FollowResponse(
    val message: String,
    val success: Boolean,
    val updatedUser: User,
)

data class UnfollowRequest(
    val followerId: String,
    val followeeId: String,
)

data class UnfollowResponse(
    val message: String,
    val success: Boolean,
    val updatedUser: User,
)

data class UpdateLastLoginResponse(
    val message: String,
    val success: Boolean,
    val user: User?,
)

data class TokenStreamRequest(
    val userId: String,
)

data class ChangepasswordRequest(
    val account: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
)

data class ResetPasswordRequest(
    val account: String = "",
    val newPassword: String = "",
)

data class ResetPasswordResponse(
    val success: Boolean,
    val message: String,
)

data class SendEmailResponse(
    val message: String,
    val code: String? = null,
)

data class SendEmailRequest(
    val email: String,
)

data class ChangepasswordResponse(
    val success: Boolean,
)

data class TokenStreamResponse(
    val streamToken: String,
)
