package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ItemGalleryBinding
import com.snapco.techlife.extensions.loadImage

class GalleryPostAdapter(
    private val images: List<String>,
    private val selectedImages: MutableList<String> = mutableListOf(),
    private val maxSelection: Int = 30,
) : RecyclerView.Adapter<GalleryPostAdapter.ViewHolder>() {
    private val selectedImagesMap = mutableMapOf<Int, Int>() // position to selection number

    init {
        // Đánh dấu các ảnh đã chọn
        selectedImages?.forEachIndexed { _, image ->
            val position = images.indexOf(image)
            if (position != -1) {
                selectedImagesMap[position] = selectedImagesMap.size + 1
            }
        }
    }

    inner class ViewHolder(
        private val binding: ItemGalleryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            s: String,
            position: Int,
        ) {
            with(binding) {
                imageView.loadImage(s)
                // Handle selection state
                val selectionNumber = selectedImagesMap[position]
                if (selectionNumber != null) {
                    selectionIndicator.setBackgroundResource(R.drawable.circle_selector1)
                    tvSelectionNumber.text = selectionNumber.toString()
                } else {
                    selectionIndicator.setBackgroundResource(R.drawable.circle_selector)
                }

                // Handle click
                itemView.setOnClickListener {
                    if (selectedImagesMap.containsKey(position)) {
                        // Bỏ chọn
                        val removedNumber = selectedImagesMap.remove(position)
                        // Cập nhật lại thứ tự các ảnh đã chọn
                        selectedImagesMap.forEach { (pos, num) ->
                            if (num > removedNumber!!) {
                                selectedImagesMap[pos] = num - 1
                            }
                        }
                    } else if (selectedImagesMap.size < maxSelection) {
                        // Select
                        selectedImagesMap[position] = selectedImagesMap.size + 1
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(images[position], position)
    }

    override fun getItemCount() = images.size

    fun getSelectedImages(): List<String> =
        selectedImagesMap
            .toList()
            .sortedBy { it.second }
            .map { images[it.first] }

    fun clearSelection() {
        selectedImagesMap.clear()
        notifyDataSetChanged()
    }

    fun getSelectedCount(): Int = selectedImagesMap.size
}
