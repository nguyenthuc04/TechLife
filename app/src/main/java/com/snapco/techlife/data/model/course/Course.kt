package com.snapco.techlife.data.model.course

data class Course(
    val id: String? = null, // ID là optional vì có thể chưa có trong khi thêm mới
    val name: String,
    val date: String,
    val price: String,
    val duration: String
)