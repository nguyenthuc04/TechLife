package com.snapco.techlife.data.response

import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.model.NotificationResponse

class NotificationRepository {
    suspend fun getNotifications(userId: String): NotificationResponse = ApiClient.apiService.getNotifications(userId)

    suspend fun updateNotificationProcessed(notificationId: String) = ApiClient.apiService.updateNotificationProcessed(notificationId)
}
