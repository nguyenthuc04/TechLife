package com.snapco.techlife.ui.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.NotificationPost
import com.snapco.techlife.databinding.ItemNotificationBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.loadImage
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationAdapter(
    private var notifications: List<NotificationPost>,
    private val onNotificationClick: onNotificationActionListener?,
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    inner class NotificationViewHolder(
        private val binding: ItemNotificationBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        @OptIn(InternalStreamChatApi::class)
        fun bind(notification: NotificationPost) {
            binding.apply {
                tvName.text = notification.nameUser
                if (notification.type == "other") {
                    tvMessage.text = "${notification.contentId}"
                    tvMessage.setTextColor(Color.BLACK)
                    tvMessage.textSize = 15F
                    ivImage.gone()
                }else if (notification.type == "like") {
                    tvMessage.text = "${notification.nameUser} đã thích bài viết của bạn"
                    ivImage.loadImage(notification.imgUser)

                } else {
                    tvMessage.text = "${notification.nameUser} đã thích reel của bạn"
                    ivImage.loadImage(notification.imgUser)
                }
                tvTime.text = getFormattedTimeDifference(notification.time)
                llItemNotification.setOnClickListener {
                    onNotificationClick?.onNotificationClick(notification)
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

    @Suppress("ktlint:standard:class-naming")
    interface onNotificationActionListener {
        fun onNotificationClick(notificationPost: NotificationPost)
    }
}
