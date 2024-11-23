package com.snapco.techlife.data.course

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CourseRetrofit {
    private const val BASE_URL = "http://26.187.200.144:3000/"

    val apiService: CourseService by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CourseService::class.java)
    }
}
