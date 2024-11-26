package com.snapco.techlife.ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snapco.techlife.data.model.Notification

class NotificationViewModel : ViewModel(){
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> get() = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        // Giả lập dữ liệu (dữ liệu thực có thể được lấy từ API hoặc Database)
//        _notifications.value = listOf(
//            Notification("Amy Nguyen", "Đã trả lời câu hỏi của bạn", "3 giờ"),
//            Notification("Bộ môn CNTT", "Đã nhắc đến bạn trong một bình luận", "7 giờ"),
//            Notification("Hữu Sơn", "Đã trả lời câu hỏi của bạn", "22 giờ"),
//            Notification("Thùy Ngân", "Đã trả lời câu hỏi của bạn", "23 giờ"),
//            Notification("Anh Kiett", "Và 2 người khác đã trả lời", "1 ngày")
//        )
    }
}