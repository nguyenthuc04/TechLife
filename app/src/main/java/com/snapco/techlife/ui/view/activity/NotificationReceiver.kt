package com.snapco.techlife.ui.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.snapco.techlife.extensions.NotificationService

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent?,
    ) {
        Log.d("NotificationReceiver", "onReceive triggered: ${intent?.action}")
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, NotificationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
                Log.d("NotificationReceiver", "Foreground service started")
            } else {
                context.startService(serviceIntent)
                Log.d("NotificationReceiver", "Service started")
            }
        }
    }
}
