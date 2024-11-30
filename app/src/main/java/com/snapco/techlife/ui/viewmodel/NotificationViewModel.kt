package com.snapco.techlife.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.model.NotificationResponse
import com.snapco.techlife.data.response.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {
    private val repository = NotificationRepository()

    private val _notificationResponse = MutableLiveData<NotificationResponse>()
    val notificationResponse: LiveData<NotificationResponse> get() = _notificationResponse

    fun getNotifications(userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getNotifications(userId)
                _notificationResponse.value = response
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "getNotifications: $e")
            }
        }
    }

    fun updateNotificationRead(notificationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.updateNotificationRead(notificationId, true)
                if (response.isSuccessful) {
                    Log.d("NotificationViewModel", "Notification processed")
                } else {
                    Log.e("NotificationViewModel", "Notification processing failed")
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "updateNotificationProcessed: $e")
            }
        }
    }
}
