package com.snapco.techlife.ui.viewmodel.objectdataholder

object UserDataHolder {
    private var userId: String? = null
    private var userAccount: String? = null
    private var userName: String? = null
    private var userAvatar: String? = null


    fun setUserData(
        id: String,
        account: String,
        name: String,
        avatar: String,

    ) {
        userId = id
        userAccount = account
        userName = name
        userAvatar = avatar

    }

    fun getUserId(): String? = userId

    fun getUserAccount(): String? = userAccount

    fun getUserName(): String? = userName

    fun getUserAvatar(): String? = userAvatar




    fun clear() {
        userId = null
        userAccount = null
        userName = null
        userAvatar = null
    }
}
