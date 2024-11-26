package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Notification

class NotificationAdapter(
    private val notifications: List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtMessage: TextView = itemView.findViewById(R.id.txtMessage)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        holder.txtName.text = notification.name
        holder.txtMessage.text = notification.message
        holder.txtTime.text = notification.time
        // Có thể load hình ảnh nếu cần
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}
