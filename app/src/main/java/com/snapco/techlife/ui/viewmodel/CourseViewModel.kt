package com.snapco.techlife.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.snapco.techlife.data.course.Course
import com.snapco.techlife.data.course.HttpRequest

class CourseViewModel(
    application: Application,
) : AndroidViewModel(application) {
    val courses = MutableLiveData<List<Course>>()
    val course = MutableLiveData<Course?>()
    val errorMessage = MutableLiveData<String>()
    val isCourseAdded = MutableLiveData<Boolean>()
    val isCourseUpdated = MutableLiveData<Boolean>()

    fun fetchCourses() {
        HttpRequest.getCourses(
            onSuccess = { courseList -> courses.value = courseList },
            onFailure = { error -> errorMessage.value = error },
        )
    }

    fun addCourse(course: Course) {
        HttpRequest.addCourse(
            course,
            onSuccess = {
                fetchCourses() // Gọi lại fetchCourses để làm mới danh sách
                isCourseAdded.value = true
            },
            onFailure = { error -> errorMessage.value = error },
        )
    }

    fun deleteCourse(courseId: String) {
        // Thêm kiểm tra cho ID
        if (courseId.isEmpty()) {
            Log.e("CourseViewModel", "Invalid courseId: $courseId")
            return
        }
        HttpRequest.deleteCourse(
            courseId,
            onSuccess = { fetchCourses() },
            onFailure = { errorMessage.value = it }, // Sửa để lấy thông báo lỗi từ phản hồi
        )
    }

    fun getCourseById(courseId: String) {
        HttpRequest.getCourseById(
            courseId,
            onSuccess = { response ->

                if (response.success) {
                    course.value = response.data
                } else {
                    errorMessage.value = response.message
                }
            },
            onFailure = { error ->
                errorMessage.value = error
            },
        )
    }

    fun fetchCoursesByUser(idUser: String) {
        HttpRequest.getCoursesByUser(
            idUser,
            onSuccess = { courseList -> courses.value = courseList },
            onFailure = { error -> errorMessage.value = error },
        )
    }

    fun updateCourse(course: Course) {
        HttpRequest.updateCourse(
            course.id!!,
            course, // Bạn cần thêm phương thức updateCourse trong HttpRequest
            onSuccess = {
                fetchCourses() // Lấy lại danh sách khóa học
                isCourseUpdated.value = true
            },
            onFailure = { error -> errorMessage.value = error },
        )
    }
}
