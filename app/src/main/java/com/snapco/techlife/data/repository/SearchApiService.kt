package com.snapco.techlife.data.repository

import com.snapco.techlife.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("getListUser")
    fun getUsers(): Call<List<User>>

    @GET("searchUser")
    fun searchUsers(@Query("name") name: String): Call<List<User>>
}