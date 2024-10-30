package com.snapco.techlife.data.model.course


import retrofit2.Call
import retrofit2.http.*

interface CourseService {

    // Lấy danh sách khóa học
    @GET("getListCourses")
    fun getCourses(): Call<List<Course>>

    // Thêm mới một khóa học
    @POST("addCourse")
    fun addCourse(@Body course: Course): Call<CourseResponse<Course>>

    // Lấy thông tin khóa học theo ID
    @GET("getCourse/{id}")
    fun getCourseById(@Path("id") courseId: String): Call<CourseResponse<Course>>

    // Cập nhật thông tin khóa học
    @PUT("/updateCourse/{id}")
    fun updateCourse(@Path("id") courseId: String, @Body course: Course): Call<CourseResponse<Course>>

    // Xóa khóa học theo ID
    @DELETE("deleteCourse/{id}")
    fun deleteCourse(@Path("id") courseId: String): Call<CourseResponse<Void>>
}
