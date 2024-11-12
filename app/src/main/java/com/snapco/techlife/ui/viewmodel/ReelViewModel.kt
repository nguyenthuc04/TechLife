package com.snapco.techlife.ui.viewmodel

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.snapco.techlife.R
import com.snapco.techlife.data.model.reel.Reel
import com.snapco.techlife.databinding.ItemReelBinding

class ReelViewModel {
    inner class VideoViewHolder(private val binding: ItemReelBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isLiked = false // Biến trạng thái để theo dõi trạng thái like

        fun bindVideo(videoModel: Reel) {
            // Binding video
            binding.videoView.apply {
                setVideoPath(videoModel.videoUrl)
                setOnPreparedListener {
                    it.start()
                    it.isLooping = true
                }
                // Play/Pause
                setOnClickListener {
                    if (isPlaying) {
                        pause()
                        binding.btnPlay.visibility = View.VISIBLE
                    } else {
                        resume()
                        binding.btnPlay.visibility = View.GONE
                    }
                }
            }

            binding.userName.text = videoModel.userName
            binding.content.text = videoModel.caption
            Glide.with(binding.root.context) // Context hiện tại (Activity hoặc Fragment)
                .load(videoModel.userImageUrl) // URL của hình ảnh
                .into(binding.imgAvata)

            binding.btnFollow.setOnClickListener {
                Toast.makeText(binding.root.context, "Đã nhấn nút Follow", Toast.LENGTH_SHORT).show()
            }
            binding.btnHeart.setOnClickListener {
                isLiked = !isLiked // Chuyển đổi trạng thái
                if (isLiked) {
                    binding.btnHeart.setImageResource(R.drawable.ic_heart_red) // Thay đổi biểu tượng thành đỏ
                    Toast.makeText(binding.root.context, "Đã nhấn nút Like", Toast.LENGTH_SHORT).show()
                } else {
                    binding.btnHeart.setImageResource(R.drawable.ic_heart) // Thay đổi biểu tượng về trạng thái ban đầu
                    Toast.makeText(binding.root.context, "Đã bỏ Like", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnComment.setOnClickListener {
                showCommentDialog(binding.root.context)
            }

            binding.btnShare.setOnClickListener {
                Toast.makeText(binding.root.context, "Đã nhấn nút Share", Toast.LENGTH_SHORT).show()
            }
        }

        private fun showCommentDialog(context: Context) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_comment_reel)

            // Tính toán kích thước màn hình
            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

            // Chiều cao màn hình
            val screenHeight = displayMetrics.heightPixels

            // Thiết lập chiều cao của dialog chiếm 2/3 chiều cao màn hình
            val dialogHeight = (screenHeight * 2) / 3

            // Thiết lập kích thước cho dialog
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT // Chiều rộng toàn màn hình
            layoutParams.height = dialogHeight // Chiều cao 2/3 màn hình
            layoutParams.gravity = Gravity.BOTTOM // Đặt dialog ở phía dưới màn hình
            dialog.window?.attributes = layoutParams

            // Xử lý danh sách bình luận
//            val comments = mutableListOf<CommentModel>() // Danh sách bình luận
//            val commentAdapter = CommentAdapter(comments)

            val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewComments)
            recyclerView.layoutManager = LinearLayoutManager(context)
//            recyclerView.adapter = commentAdapter

            val editTextComment = dialog.findViewById<EditText>(R.id.editTextComment)
            dialog.findViewById<Button>(R.id.buttonSendComment).setOnClickListener {
                val newComment = editTextComment.text.toString()
//                if (newComment.isNotEmpty()) {
//                    comments.add(CommentModel(newComment))
//                    commentAdapter.notifyDataSetChanged()
//                    editTextComment.text.clear()
//                }
            }

            dialog.show()
        }
    }
}

class ReelAdapter(
    private val videoList: List<Reel>,
) : RecyclerView.Adapter<ReelViewModel.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewModel.VideoViewHolder {
        val binding = ItemReelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReelViewModel().VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReelViewModel.VideoViewHolder, position: Int) {
        holder.bindVideo(videoList[position])
    }

    override fun getItemCount(): Int = videoList.size
}
