package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.ItemPostProfileBinding
import com.snapco.techlife.extensions.loadImage

class PostProfileAdapter(
    private var posts: List<Post>,
) : RecyclerView.Adapter<PostProfileAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PostViewHolder {
        val binding =
            ItemPostProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return PostViewHolder(binding)
    }

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
    ) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    class PostViewHolder(
        private val binding: ItemPostProfileBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.imageView9.loadImage(post.imageUrl?.get(0))
        }
    }
}
