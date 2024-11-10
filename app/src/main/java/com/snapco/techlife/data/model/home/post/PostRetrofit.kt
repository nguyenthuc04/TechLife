package com.snapco.techlife.data.model.home.post

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostRetrofit {
    private const val BASE_URL = "http://192.168.100.192:3000/"

    val apiService: PostService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostService::class.java)
    }
}
