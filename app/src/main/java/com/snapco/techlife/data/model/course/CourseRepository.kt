package com.snapco.techlife.data.model.course

import retrofit2.Call

class CourseRepository {
    private val apiService = CourseRetrofit.apiService

    suspend fun getCourses(): Call<List<Course>> {
        return apiService.getCourses()
    }

    suspend fun addCourse(course: Course): Call<CourseResponse<Course>> {
        return apiService.addCourse(course)
    }
}