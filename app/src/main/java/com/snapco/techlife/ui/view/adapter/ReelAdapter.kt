package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Reel
import com.snapco.techlife.databinding.ItemReelBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class ReelAdapter(
    private val onReelActionListener: OnReelActionListener?,
    private val recyclerView: RecyclerView,
) : PagingDataAdapter<Reel, ReelAdapter.ViewHolder>(REEL_COMPARATOR) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ItemReelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val reel = getItem(position)
        if (reel != null) {
            holder.bind(reel, position)
        }
    }

    fun loadVideoAtPosition(position: Int) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as? ViewHolder
        viewHolder?.startVideo()
    }

    private fun setLikeButtonState(
        likeButton: ImageView,
        isLiked: List<String>?,
    ) {
        val userId = UserDataHolder.getUserId()
        if (isLiked?.contains(userId) == true) {
            likeButton.setImageResource(R.drawable.ic_favorite_red) // Icon khi đã like
        } else {
            likeButton.setImageResource(R.drawable.ic_heart) // Icon khi chưa like
        }
    }

    inner class ViewHolder(
        val binding: ItemReelBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            reel: Reel,
            position: Int,
        ) {
            binding.videoView.apply {
                if (reel.videoUrl!!.isNotEmpty() && tag != reel.videoUrl!![0]) {
                    tag = reel.videoUrl[0]
                    setVideoPath(reel.videoUrl[0])
                    setOnPreparedListener {
                        it.isLooping = true
                    }
                }
                setOnClickListener {
                    if (isPlaying) {
                        pause()
                        binding.btnPlay.visible()
                    } else {
                        start()
                        binding.btnPlay.gone()
                    }
                }
            }

            binding.userName.text = reel.userName
            binding.content.text = reel.caption
            binding.imgAvata.loadImage(reel.userImageUrl)
            binding.likesCount.text = reel.likesCount.toString()
            binding.commentCount.text = reel.commentsCount.toString()

            // Set trạng thái nút like
            setLikeButtonState(binding.btnHeart, reel.likes)

            // Xử lý hành động click nút like
            binding.btnHeart.setOnClickListener {
                onReelActionListener?.onLikePost(reel, position)
            }

            // Xử lý click vào nút comment
            binding.btnComment.setOnClickListener {
                onReelActionListener?.onCommentPost(reel._id, position, reel.userId)
            }

            // Xử lý click giữ bài viết
            itemView.setOnLongClickListener {
                onReelActionListener?.onPostLongClicked(position)
                true
            }
        }

        fun startVideo() {
            if (!binding.videoView.isPlaying) {
                binding.videoView.start()
                binding.btnPlay.gone()
            }
        }

        fun updateCommentsAndLikes(reel: Reel) {
            binding.likesCount.text = reel.likesCount.toString()
            binding.commentCount.text = reel.commentsCount.toString()
            setLikeButtonState(binding.btnHeart, reel.likes)
        }

        fun pauseVideo() {
            if (binding.videoView.isPlaying) {
                binding.videoView.pause()
                binding.btnPlay.visible()
            }
        }

        fun updateLikesCount(likesCount: Int) {
            binding.likesCount.text = likesCount.toString()
        }

        fun updateCommentCount(likesCount: Int) {
            binding.commentCount.text = likesCount.toString()
        }

        fun updateLikeButton(isLiked: List<String>?) {
            setLikeButtonState(binding.btnHeart, isLiked)
        }
    }

    companion object {
        private val REEL_COMPARATOR =
            object : DiffUtil.ItemCallback<Reel>() {
                override fun areItemsTheSame(
                    oldItem: Reel,
                    newItem: Reel,
                ): Boolean = oldItem._id == newItem._id

                override fun areContentsTheSame(
                    oldItem: Reel,
                    newItem: Reel,
                ): Boolean = oldItem == newItem
            }
    }

    interface OnReelActionListener {
        fun onPostLongClicked(position: Int)

        fun onEditPost(position: Int)

        fun onDeletePost(position: Int)

        fun onLikePost(
            post: Reel,
            position: Int,
        )

        fun onCommentPost(
            postId: String,
            position: Int,
            userId: String,
        )
    }
}
