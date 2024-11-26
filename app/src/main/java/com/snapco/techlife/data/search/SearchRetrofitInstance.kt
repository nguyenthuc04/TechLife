package com.snapco.techlife.data.repository

import com.snapco.techlife.data.search.SearchApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchRetrofitInstance {
    private const val BASE_URL = "http://192.168.1.122:3000/"

    val apiService: SearchApiService by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApiService::class.java)
    }
}
