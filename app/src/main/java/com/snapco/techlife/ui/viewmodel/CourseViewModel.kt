package com.snapco.techlife.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.model.course.Course
import com.snapco.techlife.data.model.course.CourseRepository
import com.snapco.techlife.data.model.course.HttpRequest
import kotlinx.coroutines.launch

class CourseViewModel(application: Application) : AndroidViewModel(application) {
    val courses = MutableLiveData<List<Course>>()
    val errorMessage = MutableLiveData<String>()
    val isCourseAdded = MutableLiveData<Boolean>()

    fun fetchCourses() {
        HttpRequest.getCourses(
            onSuccess = { courseList -> courses.value = courseList },
            onFailure = { error -> errorMessage.value = error }
        )
    }

    fun addCourse(course: Course) {
        HttpRequest.addCourse(course,
            onSuccess = { isCourseAdded.value = true },
            onFailure = { error -> errorMessage.value = error }
        )
    }

//    fun deleteCourse(id: String) {
//        HttpRequest.deleteCourse(id,
//            onSuccess = { fetchCourses() },
//            onFailure = { error -> errorMessage.value = error }
//        )
//    }
    fun updateCourse(course: Course) {
    course.id?.let {
        HttpRequest.updateCourse(it, course,
            onSuccess = { fetchCourses() },
            onFailure = { error -> errorMessage.value = error }
        )
    }
    }
}
