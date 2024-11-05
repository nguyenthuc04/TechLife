package com.snapco.techlife.ui.viewmodel

import com.snapco.techlife.data.model.CreateUserRequest

object SignUpDataHolder {
    private var userData: CreateUserRequest? = null
    private var verificationId: String? = null

    fun setUser(user: CreateUserRequest) {
        userData = user
    }

    fun getUser(): CreateUserRequest? = userData

    fun setVerificationId(id: String) {
        verificationId = id
    }

    fun getVerificationId(): String? = verificationId

    fun clear() {
        userData = null
        verificationId = null
    }
}
