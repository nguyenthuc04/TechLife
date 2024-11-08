package com.snapco.techlife.ui.viewmodel.objectdataholder

import com.snapco.techlife.data.model.GetUserResponse

object GetUserResponseHolder {
    private var getUserResponse: GetUserResponse? = null

    fun setGetUserResponse(response: GetUserResponse) {
        getUserResponse = response
    }

    fun getGetUserResponse(): GetUserResponse? = getUserResponse

    fun clear() {
        getUserResponse = null
    }
}
