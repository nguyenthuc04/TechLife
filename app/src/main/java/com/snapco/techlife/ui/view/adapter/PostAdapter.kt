package com.snapco.techlife.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.TechlifePostBinding
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class PostAdapter(
    var modelList: MutableList<Post>,
    private val onPostActionListener: OnPostActionListener?,
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TechlifePostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun getItemCount(): Int = modelList.size

    fun updateLikeButtonAt(position: Int) {
        notifyItemChanged(position, "updateLikeButton")
    }

    fun updatePosts(newPosts: List<Post>) {
        modelList.clear()
        modelList.addAll(newPosts)
        notifyDataSetChanged()
    }

    private fun setLikeButtonState(likeButton: ImageButton, isLiked: List<String>?) {
        val userId = UserDataHolder.getUserId()
        if (isLiked?.contains(userId) == true) {
            likeButton.setImageResource(R.drawable.ic_favorite_red) // Icon for liked state
        } else {
            likeButton.setImageResource(R.drawable.ic_favorite) // Icon for not liked state
        }
    }

    inner class ViewHolder(
        private val binding: TechlifePostBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, position: Int) {
            // Bind post data
            binding.userName.text = post.userName
            binding.captionText.text = post.caption
            binding.likesCount.text = "${post.likesCount} likes"
            binding.commentsCount.text = "View all ${post.commentsCount} comments"
            binding.userImageUrl.loadImage(post.userImageUrl)
            binding.userImageUrl.loadImage(post.userImageUrl)

            binding.menuIcon.setOnClickListener {
                showBottomSheetDialog(position, itemView.context)
            }

            // Like button state
            setLikeButtonState(binding.likeButton, post.likes)

            // Handle like button click
            binding.likeButton.setOnClickListener {
                onPostActionListener?.onLikePost(post, position)
            }

            // Handle comment button click
            binding.commmentButton.setOnClickListener {
                onPostActionListener?.onCommentPost(post._id)
            }

            binding.commentsCount.setOnClickListener {
                onPostActionListener?.onCommentPost(post._id)
            }

            // Handle long click on post
            itemView.setOnLongClickListener {
                onPostActionListener?.onPostLongClicked(position)
                true
            }

            // Handle menu icon click to show BottomSheetDialog
            binding.menuIcon.setOnClickListener {
                showBottomSheetDialog(position, itemView.context)
            }

            // Set up images in RecyclerView
            val imageAdapter = ImageAdapter(post.imageUrl ?: listOf())
            binding.recyclerView.apply {
                adapter = imageAdapter
                layoutManager = LinearLayoutManager(itemView.context)
            }
        }

        private fun showBottomSheetDialog(position: Int, context: Context) {
            val bottomSheetDialog = BottomSheetDialog(context)
            val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null)
            bottomSheetDialog.setContentView(view)

            // Find views in the BottomSheet
            val editOption = view.findViewById<TextView>(R.id.option_edit)
            val deleteOption = view.findViewById<TextView>(R.id.option_delete)

            // Handle Edit action
            editOption.setOnClickListener {
                onPostActionListener?.onEditPost(position)
                bottomSheetDialog.dismiss()
            }

            // Handle Delete action
            deleteOption.setOnClickListener {
                // Show custom delete confirmation dialog
                showDeleteConfirmationDialog(context) {
                    onPostActionListener?.onDeletePost(position)
                    bottomSheetDialog.dismiss()
                }
            }

            bottomSheetDialog.show()
        }

        private fun showDeleteConfirmationDialog(context: Context, onDeleteConfirmed: () -> Unit) {
            // Inflate the custom layout
            val dialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_delete_confirmation, null)

            // Create the dialog
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
            val dialog = builder.create()

            // Set up views in the custom dialog
            val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
            val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)

            // Handle Cancel button
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            // Handle Confirm button
            btnConfirm.setOnClickListener {
                onDeleteConfirmed()
                dialog.dismiss()
            }

            // Show the dialog with transparent background
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        fun updateLikesCount(likesCount: Int) {
            binding.likesCount.text = "$likesCount likes"
        }

        fun updateLikeButton(isLiked: List<String>?) {
            setLikeButtonState(binding.likeButton, isLiked)
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