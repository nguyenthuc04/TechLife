package com.snapco.techlife.ui.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            val notificationMessage = intent.getStringExtra("notification_message")
            Log.d("NotificationReceiver", "Received notification: $notificationMessage")
            Toast.makeText(context, "Notification received: $notificationMessage", Toast.LENGTH_SHORT).show()
        }
    }
}