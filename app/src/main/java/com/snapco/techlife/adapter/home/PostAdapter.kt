package com.snapco.techlife.adapter.home

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.snapco.techlife.R
import com.snapco.techlife.data.model.home.post.Post

class PostAdapter(
    private val modelList: MutableList<Post>,
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

        // Handle like button click
        holder.likeButton.setOnClickListener {
            model.isLiked = !model.isLiked // Toggle like status
            if (model.isLiked) {
                model.likesCount += 1
            } else {
                model.likesCount -= 1
            }
            notifyItemChanged(position)
            onPostActionListener?.onLikePost(model, position) // Pass the post object and position
        }

        // Handle comment button click
        holder.commentButton.setOnClickListener {
            onPostActionListener?.onCommentPost(model.postId)
        }

        // Handle long click for menu actions
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
        val likeButton: ImageButton = itemView.findViewById(R.id.like_button)
        val commentButton: ImageButton = itemView.findViewById(R.id.comment_button)

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
                .load(userImageUrl ?: R.drawable.image_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(this.userImageUrl)

            Glide.with(itemView.context)
                .load(imageUrl ?: R.drawable.image_placeholder)
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
        fun onLikePost(post: Post, position: Int)
        fun onCommentPost(postId: String)
    }
}