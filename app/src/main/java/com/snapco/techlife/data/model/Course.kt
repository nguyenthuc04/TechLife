package com.snapco.techlife.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Course(
    @SerializedName("_id") val id: String?,
    val name: String,
    val quantity: String,
    val imageUrl: String,
    val date: String,
    val price: String,
    val duration: String,
    val describe: String,
    val userId: String?,
    val userName: String?,
    val userImageUrl: String?,
    val user: List<UserCourse>,
    val startDate: String,
    val endDate: String,
    val type: String,
    val phoneNumber: String,
)

data class UserCourse(
    val id: String,
    val date: String,
    val userName: String,
    val avatar: String,
)

data class CourseResponse(
    val success: Boolean,
    val message: String,
    val data: List<Course>,
)

data class CreateCourseRequest(
    val name: String,
    val quantity: String,
    val imageUrl: String,
    val price: String,
    val duration: String,
    val describe: String,
    val userId: String,
    val userName: String?,
    val userImageUrl: String?,
    val startDate: String,
    val endDate: String,
    val type: String,
    val phoneNumber: String,
)

@Parcelize
data class UpdateCourseRequest(
    val name: String,
    val quantity: String,
    val imageUrl: String,
    val price: String,
    val duration: String,
    val describe: String,
) : Parcelable

data class RegisterCourseRequest(
    val id: String,
    val userName: String,
    val avatar: String,
    val date: String,
)

data class UpdateCourseResponse(
    val success: Boolean,
    val message: String,
    val course: Course,
)

data class CoursesByUserResponse(
    val success: Boolean,
    val courses: List<Course>,
)

data class CourseProfileResponse(
    val success: Boolean,
    val courses: List<Course>?,
)

data class CourseDeleteResponse(
    val success: Boolean,
)
