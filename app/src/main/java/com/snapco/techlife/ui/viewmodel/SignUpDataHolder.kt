package com.snapco.techlife.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.snapco.techlife.data.model.User


object SignUpDataHolder : ViewModel() {
    private var userData: User? = null
    private var verificationId: String? = null

    fun setUser(user: User) {
        userData = user
    }

    fun getUser(): User? = userData

    fun setVerificationId(id: String) {
        verificationId = id
    }

    fun getVerificationId(): String? = verificationId

    fun clear() {
        userData = null
        verificationId = null
    }
}
