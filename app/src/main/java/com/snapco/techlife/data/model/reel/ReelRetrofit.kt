package com.snapco.techlife.data.model.reel

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ReelRetrofit {
    private const val BASE_URL = "http://192.168.100.192:3000/"

    val apiService: ReelService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReelService::class.java)
    }
}