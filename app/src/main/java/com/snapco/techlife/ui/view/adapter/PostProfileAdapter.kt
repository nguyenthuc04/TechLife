package com.snapco.techlife.ui.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.ItemPostProfileBinding
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.view.fragment.search.PostDetailFragment

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
            binding.imageView9.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("postId", post._id) // Thêm ID bài viết vào bundle
                }

                val postDetailFragment = PostDetailFragment().apply {
                    arguments = bundle
                }

                // Lấy FragmentActivity và FragmentManager để điều hướng
                itemView.context.let { context ->
                    (context as? FragmentActivity)?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.frameLayout, postDetailFragment) // Thay ID container đúng
                        ?.addToBackStack(null)
                        ?.commit()
                }
            }
        }
    }
}
