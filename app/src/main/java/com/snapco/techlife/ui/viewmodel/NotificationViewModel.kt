package com.snapco.techlife.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.model.AddNotificationRequest
import com.snapco.techlife.data.model.Notification
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationViewModel : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> get() = _notifications

    init {
        startAutoRefresh()
//        getListNotification()
//        loadNotifications()
    }

    fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                getListNotification()  // Gọi hàm để tải dữ liệu
                delay(10_000)  // Dừng 10 giây trước khi gọi lại
            }
        }
    }
    fun sendNotificationBroadcast(context: Context, message: String) {
        val intent = Intent("com.snapco.techlife.NOTIFICATION")
        intent.putExtra("notification_message", message)
        context.sendBroadcast(intent)
    }

    fun getFormattedTimeDifference(notificationTime: String): String {
        val notificationDateTime = LocalDateTime.parse(notificationTime, DateTimeFormatter.ISO_DATE_TIME)
        val now = LocalDateTime.now()
        val minutes = ChronoUnit.MINUTES.between(notificationDateTime, now)

        return when {
            minutes < 60 -> "$minutes phút trước"
            minutes < 1440 -> "${minutes / 60} giờ trước"
            else -> "${minutes / 1440} ngày trước"
        }
    }

    fun getListNotification() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getListNotification()
                if (response.success) {
                    _notifications.value = response.notifications
                        .sortedByDescending { notification ->
                            // Parse the time property to LocalDateTime for sorting
                            LocalDateTime.parse(notification.time, DateTimeFormatter.ISO_DATE_TIME)
                        }
                } else {
                    Log.e("NotificationViewModel", "Failed to load notifications")
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Get list notification failed", e)
            }
        }
    }







    fun getNotificationsByMyId(myId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getNotificationsByUser(myId)

                if (response.isSuccessful) {
                    // Nếu API trả về một Response thành công, lấy dữ liệu từ body
                    _notifications.value = response.body()
                    Log.d("NotificationViewModel", "Notifications by user: ${response.body()}")
                } else {
                    // Xử lý lỗi khi response không thành công
                    Log.e("NotificationViewModel", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Get notifications by user failed", e)
            }
        }
    }
    fun addNotification() {
        viewModelScope.launch {
            try {
                val request = AddNotificationRequest(
                    name = "John Doe",
                    message = "New message received!",
                    image = "https://example.com/images/default.jpg",
                    time = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    idPostReel = "12345",
                    myID = "user123",
                    yourID = "user456",
                    isSeen = "false"
                )

                val response = ApiClient.apiService.addNotification(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        // Log success for debugging
                        Log.d("NotificationViewModel", "Notification added: ${responseBody.notification}")
                    } else {
                        Log.e("NotificationViewModel", "Failed to add notification: ${responseBody?.message}")
                    }
                } else {
                    Log.e("NotificationViewModel", "API call failed with code: ${response.code()}")
                }

            } catch (e: HttpException) {
                println("HTTP error: ${e.message}")
            } catch (e: IOException) {
                println("Network error: ${e.message}")
            }

        }
    }
    fun addNotificationAutomatically() {
            viewModelScope.launch {
                try {
                    val request = AddNotificationRequest(
                        name = "John Doe",
                        message = "New message received!",
                        image = "https://example.com/images/default.jpg",
                        time = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        idPostReel = "12345",
                        myID = "user123",
                        yourID = "user456",
                        isSeen = "false"
                    )

                    val response = ApiClient.apiService.addNotification(request)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.success == true) {
                            // Log success for debugging
                            Log.d("NotificationViewModel", "Notification added: ${responseBody.notification}")
                        } else {
                            Log.e("NotificationViewModel", "Failed to add notification: ${responseBody?.message}")
                        }
                    } else {
                        Log.e("NotificationViewModel", "API call failed with code: ${response.code()}")
                    }

                } catch (e: HttpException) {
                    println("HTTP error: ${e.message}")
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                }

        }
    }

    //Trạng thái đã xem
    fun markAsSeen(notificationId: String) {
        viewModelScope.launch {
            try {
                // Tạo danh sách thông báo đã được cập nhật trạng thái
                val updatedNotifications = _notifications.value?.map { notification ->
                    if (notification._id == notificationId && notification.isSeen == "false") {
                        // Chuyển trạng thái isSeen thành "true" (đã xem)
                        notification.copy(isSeen = "true")
                    } else {
                        notification
                    }
                } ?: return@launch  // Trả về nếu _notifications.value là null

                // Cập nhật danh sách thông báo trong LiveData
                _notifications.value = updatedNotifications

                // Tạo request body với Map để gửi lên server
                val requestBody = mapOf("isSeen" to "true")

                // Gửi yêu cầu API để cập nhật trạng thái trên server
                val response = ApiClient.apiService.updateNotificationStatus(notificationId, requestBody)
                if (!response.isSuccessful) {
                    Log.e("NotificationViewModel", "Failed to update notification status with code: ${response.code()}")
                } else {
                    Log.d("NotificationViewModel", "Notification status updated successfully")
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error marking notification as seen", e)
            }
        }
    }








    private fun loadNotifications() {
        // Tải danh sách thông báo (ví dụ: từ API hoặc Database)
        val mockData = listOf(
            Notification(
                _id = "1",
                name = "John Doe",
                message = "Your post has been liked!",
                image = "https://example.com/images/profile1.jpg",
                time = "2024-11-26T14:30:00Z",
                idPostReel = "64abf1823f7c9d0012341234",
                myID = "64abf1823f7c9d0012345678",
                yourID = "64abf1823f7c9d0012345679",
                isSeen = "false"
            ),
            Notification(
                _id = "2",
                name = "Jane Smith",
                message = "You have a new follower!",
                image = "https://example.com/images/profile2.jpg",
                time = "2024-11-27T12:30:00Z",
                idPostReel = "post123",
                myID = "64abf1823f7c9d0012345678",
                yourID = "user456",
                isSeen = "true"
            ),
            Notification(
                _id = "3",
                name = "Alice Brown",
                message = "Commented on your photo",
                image = "https://example.com/images/profile3.jpg",
                time = "2024-11-27T15:00:00Z",
                idPostReel = "post456",
                myID = "64abf1823f7c9d0012345678",
                yourID = "user789",
                isSeen = "false"
            )
        )
        _notifications.value = mockData
    }
}
