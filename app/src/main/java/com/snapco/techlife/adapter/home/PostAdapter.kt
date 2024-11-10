package com.snapco.techlife.adapter.home

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.snapco.techlife.R
import com.snapco.techlife.data.model.home.Post

class PostAdapter(
    private val modelList: List<Post>,
    private val onPostActionListener: OnPostActionListener?
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.techlife_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        holder.setItems(
            model.userImageUrl,
            model.userName,
            model.caption,
            model.imageUrl,
            model.likesCount,
            model.commentsCount,
            model.createdAt
        )

        holder.itemView.setOnLongClickListener {
            onPostActionListener?.onPostLongClicked(position)
            true
        }
    }

    override fun getItemCount(): Int = modelList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userImageUrl: ImageView = itemView.findViewById(R.id.user_image_url)
        private val imageUrl: ImageView = itemView.findViewById(R.id.image_url)
        private val userName: TextView = itemView.findViewById(R.id.user_name)
        private val captionText: TextView = itemView.findViewById(R.id.caption_text)
        private val likesCount: TextView = itemView.findViewById(R.id.likes_count)
        private val commentsCount: TextView = itemView.findViewById(R.id.comments_count)
        private val menuIcon: ImageView = itemView.findViewById(R.id.menu_icon)

        init {
            menuIcon.setOnClickListener { showPopupMenu(it) }
        }

        private fun showPopupMenu(view: View) {
            val popup = PopupMenu(view.context, view)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.popup_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        onPostActionListener?.onEditPost(adapterPosition)
                        true
                    }
                    R.id.action_delete -> {
                        onPostActionListener?.onDeletePost(adapterPosition)
                        true
                    }
                    R.id.action_share -> {
                        Toast.makeText(view.context, "Share clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        fun setItems(
            userImageUrl: String?,
            userName: String,
            caption: String,
            imageUrl: String?,
            likesCount: Int,
            commentsCount: Int,
            createdAt: String
        ) {
            Glide.with(itemView.context)
                .load(userImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.image_placeholder) // Placeholder if user image fails
                .into(this.userImageUrl)

            Glide.with(itemView.context)
                .load(imageUrl)
                .error(R.drawable.image_placeholder) // Placeholder if post image fails
                .into(this.imageUrl)

            this.userName.text = userName
            this.captionText.text = caption
            this.likesCount.text = "$likesCount likes"
            this.commentsCount.text = "View all $commentsCount comments"
        }
    }

    interface OnPostActionListener {
        fun onPostLongClicked(position: Int)
        fun onEditPost(position: Int)
        fun onDeletePost(position: Int)
    }
}
