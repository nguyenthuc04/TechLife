package com.snapco.techlife.data.search

import com.snapco.techlife.data.model.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("searchUser")
    fun searchUsers(@Query("name") name: String): Call<List<SearchUserResponse>>
}