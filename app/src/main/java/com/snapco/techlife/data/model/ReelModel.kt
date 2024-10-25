package com.snapco.techlife.data.model

data class ReelModel(
    val url: String,              // URL của video
    val userName: String,         // Tên người dùng
    val content: String,          // Nội dung mô tả video
    val isFollowed: Boolean = false // Trạng thái theo dõi (mặc định là false)
)