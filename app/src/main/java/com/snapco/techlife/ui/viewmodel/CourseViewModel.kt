package com.snapco.techlife.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.data.model.CourseDeleteResponse
import com.snapco.techlife.data.model.CourseProfileResponse
import com.snapco.techlife.data.model.CourseResponse
import com.snapco.techlife.data.model.CoursesByUserResponse
import com.snapco.techlife.data.model.CreateCourseRequest
import com.snapco.techlife.data.model.RegisterCourseRequest
import com.snapco.techlife.data.model.UpdateCourseRequest
import com.snapco.techlife.data.model.UpdateCourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {
    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> get() = _courses

    private val _createCourseResponse = MutableLiveData<CourseResponse>()
    val createCourseResponse: LiveData<CourseResponse> get() = _createCourseResponse

    private val _courseUserResponse = MutableLiveData<CourseProfileResponse>()
    val courseUserResponse: LiveData<CourseProfileResponse> get() = _courseUserResponse

    private val _updateCourseResponse = MutableLiveData<UpdateCourseResponse>()
    val updateCourseResponse: LiveData<UpdateCourseResponse> get() = _updateCourseResponse

    private val _coursesByUserResponse = MutableLiveData<CoursesByUserResponse>()
    val coursesByUserResponse: LiveData<CoursesByUserResponse> get() = _coursesByUserResponse

    private val _registerCourseResponse = MutableLiveData<UpdateCourseResponse>()
    val registerCourseResponse: LiveData<UpdateCourseResponse> get() = _registerCourseResponse

    private val _deleteCourseResponse = MutableLiveData<CourseDeleteResponse>()
    val deleteCourseResponse: LiveData<CourseDeleteResponse> get() = _deleteCourseResponse

    private val _courseDetails = MutableLiveData<Course>()
    val coursesDetails: LiveData<Course> get() = _courseDetails

    fun setCours(course: Course) {
        _courseDetails.value = course
    }

    private val _searchCourses = MutableLiveData<List<Course>>()
    val searchCourses: LiveData<List<Course>> = _searchCourses

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getCoursesByUserRegistration(courseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getCoursesByUserRegistration(courseId)
                _coursesByUserResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Update course failed", e)
            }
        }
    }

    fun deleteCourse(courseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.deleteCourse(courseId)
                if (response.isSuccessful) {
                    _deleteCourseResponse.postValue(CourseDeleteResponse(success = true))
                } else {
                    _deleteCourseResponse.postValue(CourseDeleteResponse(success = false))
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Delete course failed", e)
                _deleteCourseResponse.postValue(CourseDeleteResponse(success = false))
            }
        }
    }

    fun updateCourse(
        courseId: String,
        updateCourseRequest: UpdateCourseRequest,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.updateCourse(courseId, updateCourseRequest)
                _updateCourseResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Update course failed", e)
            }
        }
    }

    fun registerCourse(
        courseId: String,
        registerCourseRequest: RegisterCourseRequest,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.registerCourse(courseId, registerCourseRequest)
                _registerCourseResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Register course failed", e)
                // Xử lý lỗi, ví dụ đăng kết quả lỗi
                _error.postValue(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    fun getCoursesByUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getCoursesByUser(userId)
                _courseUserResponse.value = response
                Log.d("UserViewModel", "Get user success" + response)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Get user failed", e)
            }
        }
    }

    fun createCourse(createCourseRequest: CreateCourseRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.createCourse(createCourseRequest)
                _createCourseResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Create Post failed", e)
            }
        }
    }

    fun getListCourses() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getCourses()
                _courses.value = response
            } catch (e: Exception) {
                Log.e("UserViewModel", "Get list users failed", e)
            }
        }
    }

    fun searchCourses(name: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.searchCourses(name)

                if (response.success) {
                    _searchCourses.value = response.data
                } else {
                    _searchCourses.value = emptyList()
                    // Xử lý lỗi từ backend
                    _error.value = response.message
                }
            } catch (e: Exception) {
                _searchCourses.value = emptyList()
                // Xử lý các lỗi ngoại lệ khác
                _error.value = e.localizedMessage ?: "Đã xảy ra lỗi không xác định."
            }
        }
    }
}
