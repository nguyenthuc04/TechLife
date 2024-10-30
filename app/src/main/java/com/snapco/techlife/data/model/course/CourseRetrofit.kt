package com.snapco.techlife.data.model.course


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CourseRetrofit {
    private const val BASE_URL = "http://10.0.2.2:3000/" // Thay bằng URL của server

    val apiService: CourseService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CourseService::class.java)
    }
}