package com.snapco.techlife.data.model.course


import retrofit2.Call
import retrofit2.http.*

interface CourseService {


    @GET("getListCourses")
    fun getCourses(): Call<List<Course>>


    @POST("addCourse")
    fun addCourse(@Body course: Course): Call<CourseResponse<Course>>


    @GET("getCourse/{id}")
    fun getCourseById(@Path("id") courseId: String): Call<CourseResponse<Course>>


    @PUT("updateCourse/{id}")
    fun updateCourse(@Path("id") courseId: String, @Body course: Course): Call<CourseResponse<Course>>

    @GET("getCoursesByUser/{idUser}")
    fun getCoursesByUser(@Path("idUser") idUser: String): Call<List<Course>>


    @DELETE("deleteCourse/{id}")
    fun deleteCourse(@Path("id") id: String): Call<Void>
}
