package com.snapco.techlife.ui.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.data.model.ReelModel
import com.snapco.techlife.databinding.ItemReelBinding
import com.snapco.techlife.ui.view.fragment.reels.AddReelFragment

class ReelViewModel {
    inner class VideoViewHolder(private val binding: ItemReelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindVideo(videoModel: ReelModel) {

            //bindingVideo
            binding.videoView.apply {
                setVideoPath(videoModel.url)
                setOnPreparedListener{
                    it.start()
                    it.isLooping = true
                }
                //play pause
                setOnClickListener{
                    if (isPlaying){
                        pause()
                        binding.btnPlay.visibility = View.VISIBLE
                    }else{
                        resume()
                        binding.btnPlay.visibility = View.GONE
                    }
                }

             }

            binding.userName.text = "Tên người dùng"
            binding.content.text = "Nội dung video"

            binding.btnFollow.setOnClickListener {
                // Xử lý sự kiện khi click vào nút Follow
            }
            binding.btnHeart.setOnClickListener {
                // Logic cho nút Heart
            }

            binding.btnComment.setOnClickListener {
                // Logic cho nút Comment
            }

            binding.btnShare.setOnClickListener {
                // Logic cho nút Share
            }
        }


    }
}

class ReelAdapter(
    private val videoList: List<ReelModel>,
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
