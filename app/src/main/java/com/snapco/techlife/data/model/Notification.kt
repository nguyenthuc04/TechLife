package com.snapco.techlife.data.model

data class Notification(
    val _id: String,
    val name: String,
    val message: String,
    val image: String,
    val time: String,
    val idPostReel: String,
    val myID: String,
    val yourID: String,
    val isSeen: String = "false",

    )
data class NotificationResponse(
    val success: Boolean,
    val notifications: List<Notification>
)

data class AddNotificationRequest(
    val name: String,
    val message: String,
    val image: String,
    val time: String,
    val idPostReel: String? = null,
    val myID: String,
    val yourID: String,
    val isSeen: String
)

data class AddNotificationResponse(
    val success: Boolean,
    val message: String,
    val notification: Notification?
)
