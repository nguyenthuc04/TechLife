package com.snapco.techlife.data.model.course


data class CourseResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)