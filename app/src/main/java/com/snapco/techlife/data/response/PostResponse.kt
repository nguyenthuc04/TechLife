package com.snapco.techlife.data.response

import com.snapco.techlife.data.model.Post

data class PostResponse(
    val post: Post,
    val success: Boolean,
    val message: String
)

