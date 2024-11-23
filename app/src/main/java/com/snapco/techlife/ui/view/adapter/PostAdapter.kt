package com.snapco.techlife.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.TechlifePostBinding
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class PostAdapter(
    private var modelList: MutableList<Post>,
    private val onPostActionListener: OnPostActionListener?,
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            TechlifePostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty()) {
            holder.bind(modelList[position], position)
        } else {
            for (payload in payloads) {
                if (payload == "updateLikeButton") {
                    holder.updateLikeButton(modelList[position].likes)
                    holder.updateLikesCount(modelList[position].likesCount)
                }
            }
        }
    }

    fun updateLikeButtonAt(position: Int) {
        notifyItemChanged(position, "updateLikeButton")
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        onBindViewHolder(holder, position, mutableListOf())
        val post = modelList[position]
        holder.bind(post, position)
    }

    override fun getItemCount(): Int = modelList.size

    fun updatePosts(newPosts: List<Post>) {
        modelList.clear()
        modelList.addAll(newPosts)
        notifyDataSetChanged()
    }

    private fun setLikeButtonState(
        likeButton: ImageButton,
        isLiked: List<String>?,
    ) {
        val userId = UserDataHolder.getUserId()
        if (isLiked?.contains(userId) == true) {
            likeButton.setImageResource(R.drawable.ic_favorite_red) // Icon khi đã like
        } else {
            likeButton.setImageResource(R.drawable.ic_favorite) // Icon khi chưa like
        }
    }

    inner class ViewHolder(
        private val binding: TechlifePostBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            post: Post,
            position: Int,
        ) {
            // Hiển thị dữ liệu post
            binding.userName.text = post.userName
            binding.captionText.text = post.caption
            binding.likesCount.text = "${post.likesCount} lượt thích"
            binding.commentsCount.text = "Xem tất cả ${post.commentsCount} bình luận"
            binding.userImageUrl.loadImage(post.userImageUrl)

            // Set trạng thái nút like
            setLikeButtonState(binding.likeButton, post.likes)

            // Xử lý hành động click nút like
            binding.likeButton.setOnClickListener {
                onPostActionListener?.onLikePost(post, position)
            }
            Log.d("HomeViewModel", "Post: $post")
            // Xử lý click vào nút comment
            binding.commmentButton.setOnClickListener {
                onPostActionListener?.onCommentPost(post._id)
            }
            binding.commentsCount.setOnClickListener {
                onPostActionListener?.onCommentPost(post._id)
            }
            // Xử lý click giữ bài viết
            itemView.setOnLongClickListener {
                onPostActionListener?.onPostLongClicked(position)
                true
            }

            // Hiển thị danh sách hình ảnh nếu có
            val imageAdapter = ImageAdapter(post.imageUrl ?: listOf())
            binding.recyclerView.apply {
                adapter = imageAdapter
                layoutManager = LinearLayoutManager(itemView.context)
            }
        }

        fun updateLikesCount(likesCount: Int) {
            binding.likesCount.text = "$likesCount lượt thích"
        }

        fun updateLikeButton(isLiked: List<String>?) {
            setLikeButtonState(binding.likeButton, isLiked)
        }
    }

    interface OnPostActionListener {
        fun onPostLongClicked(position: Int)

        fun onEditPost(position: Int)

        fun onDeletePost(position: Int)

        fun onLikePost(
            post: Post,
            position: Int,
        )

        fun onCommentPost(postId: String)
    }
}
