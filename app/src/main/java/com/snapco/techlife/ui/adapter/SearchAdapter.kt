package com.snapco.techlife.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.data.model.User

class SearchAdapter(private var items: List<User>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView) // TextView hiển thị tên người dùng
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView) // TextView hiển thị email người dùng
    }

    // Phương thức này để inflate layout item và tạo một instance của ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_user, parent, false)
        return ViewHolder(view)
    }

    // Phương thức này để gán dữ liệu từ đối tượng User vào các view trong ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.nameTextView.text = user.name // Gán tên người dùng vào TextView
        holder.emailTextView.text = user.email // Gán email người dùng vào TextView
    }

    // Phương thức này trả về kích thước của danh sách items
    override fun getItemCount(): Int = items.size

    // Phương thức này cập nhật danh sách items với dữ liệu mới và làm mới RecyclerView
    fun updateData(newItems: List<User>) {
        items = newItems
        notifyDataSetChanged() // Thông báo cho adapter rằng dữ liệu đã thay đổi
    }
}