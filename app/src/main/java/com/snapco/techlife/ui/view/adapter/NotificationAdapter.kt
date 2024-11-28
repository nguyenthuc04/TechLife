package com.snapco.techlife.ui.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Notification
import com.snapco.techlife.databinding.ItemNotificationBinding
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.ui.utils.load
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationAdapter(
    private val notifications: List<Notification>,
    private val onClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @OptIn(InternalStreamChatApi::class)
        fun bind(notification: Notification) {
            binding.apply {
                tvName.text = notification.name
                tvMessage.text = notification.message
                tvTime.text = getFormattedTimeDifference(notification.time)
                ivImage.load(notification.image) // Sử dụng thư viện như Glide hoặc Coil
                root.setOnClickListener { onClick(notification) }

                if (notification.isSeen == "false") { // Check for false explicitly
                    root.setBackgroundColor(Color.parseColor("#D6EAF8"))
                } else {
                    root.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }

        }

        fun getFormattedTimeDifference(notificationTime: String): String {
            val notificationDateTime = LocalDateTime.parse(notificationTime, DateTimeFormatter.ISO_DATE_TIME)
            val now = LocalDateTime.now()
            val seconds = ChronoUnit.SECONDS.between(notificationDateTime, now)
            val minutes = seconds / 60

            return when {
                seconds < 60 -> "$seconds giây trước"
                minutes < 60 -> "$minutes phút trước"
                minutes < 1440 -> "${minutes / 60} giờ trước"
                else -> "${minutes / 1440} ngày trước"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size
}
