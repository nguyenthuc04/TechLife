package com.snapco.techlife.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.snapco.techlife.data.course.HttpRequest
import com.snapco.techlife.data.model.course.Course

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
                if (course.idUser != null) {
                    fetchCoursesByUser(course.idUser) // Gọi lại fetchCourses để làm mới danh sách
                    isCourseAdded.value = true
                } else {
                    errorMessage.value = "Lỗi: Không có thông tin người dùng"
                }
            },
            onFailure = { error -> errorMessage.value = error },
        )
    }

    fun deleteCourse(
        courseId: String,
        idUser: String,
    ) {
        if (courseId.isEmpty()) {
            Log.e("CourseViewModel", "Invalid courseId: $courseId")
            return
        }
        HttpRequest.deleteCourse(
            courseId,
            onSuccess = {
                fetchCoursesByUser(idUser) // Lấy danh sách khóa học theo idUser
            },
            onFailure = { errorMessage.value = it },
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
            course,
            onSuccess = {
                fetchCoursesByUser(course.idUser!!) // Lấy danh sách theo idUser của khóa học
                isCourseUpdated.value = true
            },
            onFailure = { error -> errorMessage.value = error },
        )
    }
}
