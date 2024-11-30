package com.snapco.techlife.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.startForeground
import androidx.core.content.ContextCompat.getSystemService
import com.snapco.techlife.R
import com.snapco.techlife.data.response.NotificationRepository
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 5000L // Kiểm tra mỗi 5 giây
    private val repository = NotificationRepository()
    private val accountManager by lazy { AccountManager(this) }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        startForegroundNotification()
        handler.post(checkForNotifications)
        return START_STICKY
    }

    private val checkForNotifications =
        object : Runnable {
            override fun run() {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val idUser = UserDataHolder.getUserId().toString()
                        Log.d("NotificationService12", "User ID: $idUser")
                        val response = repository.getNotifications(idUser)
                        if (response.notifications.isNotEmpty()) {
                            for (notification in response.notifications) {
                                Log.d("NotificationService", "Notification: $notification")

                                if (notification.processed == false) {
                                    showNotification(
                                        notification.nameUser,
                                        (
                                            if (notification.type == "like" && notification.contentType == "post") {
                                                "đã thích bài viết của bạn"
                                            } else if (notification.type == "like" && notification.contentType == "reel") {
                                                "đã thích reel của bạn"
                                            } else if (notification.type == "comment" && notification.contentType == "post") {
                                                "đã bình luận bài viết của bạn"
                                            } else if (notification.type == "comment" && notification.contentType == "reel") {
                                                "đã bình luận reel của bạn"
                                            } else {
                                                ""
                                            }
                                        ).toString(),
                                        notification._id,
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("NotificationService", "Error fetching notifications: $e")
                    }
                }
                handler.postDelayed(this, checkInterval)
            }
        }

    private fun startForegroundNotification() {
        val channelId = "notification_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId,
                    "Foreground Service Notifications",
                    NotificationManager.IMPORTANCE_LOW,
                )
            notificationManager.createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat
                .Builder(this, channelId)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.techlife_logo)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

        startForeground(1, notification)
    }

    private suspend fun showNotification(
        title: String,
        message: String,
        notificationId: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.updateNotificationProcessed(notificationId)
            } catch (e: Exception) {
                Log.e("NotificationService", "Error updating notification: $e")
            }
        }
        val channelId = "notification_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification =
            NotificationCompat
                .Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.techlife_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        notificationManager.notify(notificationId.hashCode(), notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
