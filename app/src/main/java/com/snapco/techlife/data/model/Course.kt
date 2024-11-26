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
)

data class UserCourse(
    val userId: String,
    val name: String,
    val date: String,
    val status: String,
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

data class UpdateCourseResponse(
    val success: Boolean,
    val message: String,
    val course: Course,
)

data class CourseProfileResponse(
    val success: Boolean,
    val courses: List<Course>?,
)

data class CourseDeleteResponse(
    val success: Boolean,
)
