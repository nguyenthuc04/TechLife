package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Comment
import com.snapco.techlife.databinding.ItemCommentBinding
import com.snapco.techlife.extensions.formatRelativeTime
import com.snapco.techlife.extensions.loadImage

class CommentAdapter(
    private var comment: List<Comment>,
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    fun updateAccounts(newComments: List<Comment>) {
        comment = newComments
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int,
    ) {
        holder.bind(comment[position])
    }

    override fun getItemCount(): Int = comment.size

    class CommentViewHolder(
        private val binding: ItemCommentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.textView30.text = comment.userName
            binding.textView31.text = formatRelativeTime(comment.createdAt)
            binding.textView32.text = comment.text
            binding.imgAvatar.loadImage(comment.userImageUrl)
        }
    }
}