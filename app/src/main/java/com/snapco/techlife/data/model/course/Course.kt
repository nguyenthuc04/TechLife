package com.snapco.techlife.data.model.course

import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("_id")
    val id: String?,
    val name: String,
    val price: String,
    val duration: String,
    val date: String
)