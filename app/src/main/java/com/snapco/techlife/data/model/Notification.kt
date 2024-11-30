package com.snapco.techlife.data.model

data class NotificationPost(
    val _id: String,
    val contentId: String,
    val userId: String,
    val imgUser: String,
    val nameUser: String,
    val yourID: String,
    val time: String,
    val read: Boolean,
    val processed: Boolean,
    val type: String,
    val contentType: String,
)

data class LikeNotificationRequest(
    val userId: String,
    val imgUser: String,
    val nameUser: String,
    val yourID: String,
)

data class LikeReelNotificationRequest(
    val userId: String,
    val imgUser: String,
    val nameUser: String,
    val yourID: String,
)

data class NotificationResponse(
    val success: Boolean,
    val notifications: List<NotificationPost>,
)
