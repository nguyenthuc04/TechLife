package com.snapco.techlife.ui.view.adapter

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Reel
import com.snapco.techlife.databinding.ItemReelProfileBinding

class ReelProfileAdapter(
    private var reels: List<Reel>,
    private val onReelClickListener: (Reel) -> Unit,
) : RecyclerView.Adapter<ReelProfileAdapter.ReelViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReelViewHolder {
        val binding =
            ItemReelProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ReelViewHolder(binding)
    }

    fun updateReels(newReels: List<Reel>) {
        reels = newReels
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: ReelViewHolder,
        position: Int,
    ) {
        val reel = reels[position]
        holder.bind(reel)
        holder.itemView.setOnClickListener {
            onReelClickListener(reel)
        }
    }

    override fun getItemCount(): Int = reels.size

    class ReelViewHolder(
        private val binding: ItemReelProfileBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reel: Reel) {
            val videoUrl = reel.videoUrl?.get(0)
            if (videoUrl != null) {
                val thumbnail = getVideoThumbnail(videoUrl)
                if (thumbnail != null) {
                    // Gán thumbnail vào ImageView
                    binding.videoView.setImageBitmap(thumbnail)
                }
            }
        }

        private fun getVideoThumbnail(videoPath: String): Bitmap? =
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(videoPath)
                // Lấy thumbnail tại thời điểm đầu tiên của video
                val bitmap = retriever.frameAtTime
                retriever.release()
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
    }
}
