package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.NotificationPost
import com.snapco.techlife.databinding.ItemNotificationBinding
import com.snapco.techlife.extensions.loadImage
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationAdapter(
    private var notifications: List<NotificationPost>,
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    inner class NotificationViewHolder(
        private val binding: ItemNotificationBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        @OptIn(InternalStreamChatApi::class)
        fun bind(notification: NotificationPost) {
            binding.apply {
                tvName.text = notification.nameUser
                tvMessage.text =
                    if (notification.type == "like") {
                        "${notification.nameUser} đã thích bài viết của bạn"
                    } else {
                        "${notification.nameUser} đã thích reel của bạn"
                    }
                tvTime.text = getFormattedTimeDifference(notification.time)
                ivImage.loadImage(notification.imgUser)
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return NotificationViewHolder(binding)
    }

    fun updateNotifications(notificationPost: List<NotificationPost>) {
        notifications = notificationPost
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int,
    ) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size
}
