package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.User
import com.snapco.techlife.databinding.ItemListAccBinding

class ListAccAdapter : RecyclerView.Adapter<ListAccAdapter.ListAccViewHodel>() {
    private var userList: List<User> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ListAccViewHodel {
        val binding = ItemListAccBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListAccViewHodel(binding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(
        holder: ListAccViewHodel,
        position: Int,
    ) {
        holder.bind(userList[position])
    }

    inner class ListAccViewHodel(
        private val binding: ItemListAccBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
        }
    }
}
