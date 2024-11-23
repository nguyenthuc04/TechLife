package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Reel
import com.snapco.techlife.databinding.ItemReelProfileBinding

class ReelProfileAdapter(
    private var reels: List<Reel>,
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
    }

    override fun getItemCount(): Int = reels.size

    class ReelViewHolder(
        private val binding: ItemReelProfileBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reel: Reel) {
            binding.videoView.setVideoPath(reel.videoUrl?.get(0))
        }
    }
}
