package com.snapco.techlife.data.model

data class User(
    val id: String = "",
    val account: String = "",
    val password: String = "",
    val birthday: String = "",
    val name: String = "",
    val nickname: String = "",
    val avatar: String = "",
    val following: Int = 0,
    val followers: Int = 0,
    val bio: String = "",
    val posts: Int = 0,
)
