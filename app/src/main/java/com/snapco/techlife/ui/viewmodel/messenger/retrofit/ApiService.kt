package com.snapco.techlife.ui.viewmodel.messenger.retrofit

import com.snapco.techlife.ui.viewmodel.messenger.LoginRequest
import com.snapco.techlife.ui.viewmodel.messenger.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}