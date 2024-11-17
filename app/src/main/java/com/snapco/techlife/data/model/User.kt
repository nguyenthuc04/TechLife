package com.snapco.techlife.data.model

import android.os.Parcel
import android.os.Parcelable

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
)

data class UpdateUserResponse(
    val message: String,
    val user: User,
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
)

data class CreateUserResponse(
    val message: String,
    val user: User,
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
