package com.snapco.techlife.data.model.course


import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HttpRequest {
    fun getCourses(onSuccess: (List<Course>) -> Unit, onFailure: (String) -> Unit) {
        val call = CourseRetrofit.apiService.getCourses()
        call.enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: emptyList())
                } else {
                    onFailure("Lỗi khi tải danh sách khóa học.")
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                onFailure("Lỗi kết nối!")
            }
        })
    }
    fun getCourseById(courseId: String, onSuccess: (CourseResponse<Course>) -> Unit, onFailure: (String) -> Unit) {
        val call = CourseRetrofit.apiService.getCourseById(courseId)
        call.enqueue(object : Callback<CourseResponse<Course>> {
            override fun onResponse(call: Call<CourseResponse<Course>>, response: Response<CourseResponse<Course>>) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!) // Gọi callback onSuccess với dữ liệu phản hồi
                } else {
                    // Nếu không thành công, trả về thông báo từ phản hồi
                    val errorMessage = response.errorBody()?.string() ?: "Lỗi không xác định."
                    onFailure(errorMessage) // Gọi callback onFailure với thông báo lỗi
                }
            }

            override fun onFailure(call: Call<CourseResponse<Course>>, t: Throwable) {
                onFailure("Lỗi kết nối!") // Gọi callback onFailure nếu gặp lỗi kết nối
            }
        })
    }


    fun addCourse(course: Course, onSuccess: (Course) -> Unit, onFailure: (String) -> Unit) {
        val call = CourseRetrofit.apiService.addCourse(course)
        call.enqueue(object : Callback<CourseResponse<Course>> {
            override fun onResponse(call: Call<CourseResponse<Course>>, response: Response<CourseResponse<Course>>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()?.data!!)
                } else {
                    onFailure("Lỗi khi thêm khóa học.")
                }
            }

            override fun onFailure(call: Call<CourseResponse<Course>>, t: Throwable) {
                onFailure("Lỗi kết nối!")
            }
        })
    }

    fun deleteCourse(id: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val call = CourseRetrofit.apiService.deleteCourse(id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure("Lỗi khi xóa khóa học.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure("Lỗi kết nối!")
            }
        })
    }
    fun updateCourse(courseId: String, course: Course, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val call = CourseRetrofit.apiService.updateCourse(courseId, course) // Thay đổi với endpoint đúng
        call.enqueue(object : Callback<CourseResponse<Course>> {
            override fun onResponse(call: Call<CourseResponse<Course>>, response: Response<CourseResponse<Course>>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure("Lỗi khi cập nhật khóa học.")
                }
            }

            override fun onFailure(call: Call<CourseResponse<Course>>, t: Throwable) {
                onFailure("Lỗi kết nối!")
            }
        })
    }

    fun getCoursesByUser(
        idUser: String,
        onSuccess: (List<Course>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val call = CourseRetrofit.apiService.getCoursesByUser(idUser)
        call.enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onFailure("Failed to fetch courses")
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                onFailure(t.message ?: "Unknown error")
            }
        })
    }




}
