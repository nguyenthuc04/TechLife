package com.snapco.techlife.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.model.course.Course
import com.snapco.techlife.data.model.course.HttpRequest
import kotlinx.coroutines.launch

class CourseViewModel(application: Application) : AndroidViewModel(application) {
    val courses = MutableLiveData<List<Course>>()
    val course = MutableLiveData<Course?>()
    val errorMessage = MutableLiveData<String>()
    val isCourseAdded = MutableLiveData<Boolean>()
    val isCourseUpdated = MutableLiveData<Boolean>()

    fun fetchCourses() {
        HttpRequest.getCourses(
            onSuccess = { courseList -> courses.value = courseList },
            onFailure = { error -> errorMessage.value = error }
        )
    }

    fun addCourse(course: Course) {
        HttpRequest.addCourse(course,
            onSuccess = {
                fetchCourses() // Gọi lại fetchCourses để làm mới danh sách
                isCourseAdded.value = true
            },
            onFailure = { error -> errorMessage.value = error }
        )
    }
    fun deleteCourse(courseId: String) {
        // Thêm kiểm tra cho ID
        if (courseId.isEmpty()) {
            Log.e("CourseViewModel", "Invalid courseId: $courseId")
            return
        }
        HttpRequest.deleteCourse(courseId,
            onSuccess = { fetchCourses() },
            onFailure = { errorMessage.value = it } // Sửa để lấy thông báo lỗi từ phản hồi
        )
    }
    fun updateCourse(course: Course) {
        if (course.id.isNullOrEmpty()) {
            errorMessage.value = "ID khóa học không hợp lệ!"
            return
        }
        HttpRequest.updateCourse(course.id, course,
            onSuccess = {
                fetchCourses() // Cập nhật danh sách khóa học
                isCourseUpdated.value = true
            },
            onFailure = { error -> errorMessage.value = error }
        )
    }
    fun getCourseById(courseId: String) {
        HttpRequest.getCourseById(courseId,
            onSuccess = { response ->
                // Kiểm tra xem phản hồi có thành công không
                if (response.success) {
                    course.value = response.data // Lưu thông tin khóa học vào LiveData
                } else {
                    errorMessage.value = response.message // Lưu thông báo lỗi vào LiveData
                }
            },
            onFailure = { error ->
                errorMessage.value = error // Lưu thông báo lỗi từ callback vào LiveData
            }
        )
    }

//    fun updateCourse(course: Course) {
//    course.id?.let {
//        HttpRequest.updateCourse(it, course,
//            onSuccess = { fetchCourses() },
//            onFailure = { error -> errorMessage.value = error }
//        )
//    }
//    }
}
