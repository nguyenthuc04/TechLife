package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snapco.techlife.R
import com.snapco.techlife.extensions.ImageLayoutType
import com.snapco.techlife.extensions.ImageViewHolder

class ImageAdapter(
    private val images: List<String>,
) : RecyclerView.Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (ImageLayoutType.values()[viewType]) {
            ImageLayoutType.SINGLE ->
                ImageViewHolder.SingleImageViewHolder(
                    inflater.inflate(R.layout.layout_signle_image, parent, false),
                )
            ImageLayoutType.TWO ->
                ImageViewHolder.TwoImagesViewHolder(
                    inflater.inflate(R.layout.layout_two_images, parent, false),
                )
            ImageLayoutType.THREE ->
                ImageViewHolder.ThreeImagesViewHolder(
                    inflater.inflate(R.layout.layout_three_images, parent, false),
                )
            ImageLayoutType.FOUR ->
                ImageViewHolder.FourImagesViewHolder(
                    inflater.inflate(R.layout.layout_four_images, parent, false),
                )
            ImageLayoutType.FIVE_OR_MORE ->
                ImageViewHolder.FiveOrMoreImagesViewHolder(
                    inflater.inflate(R.layout.layout_five_or_more_images, parent, false),
                )
        }
    }

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int,
    ) {
        when (holder) {
            is ImageViewHolder.SingleImageViewHolder -> bindSingleImage(holder, position)
            is ImageViewHolder.TwoImagesViewHolder -> bindTwoImages(holder, position)
            is ImageViewHolder.ThreeImagesViewHolder -> bindThreeImages(holder, position)
            is ImageViewHolder.FourImagesViewHolder -> bindFourImages(holder, position)
            is ImageViewHolder.FiveOrMoreImagesViewHolder -> bindFiveOrMoreImages(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when {
            images.size == 1 -> ImageLayoutType.SINGLE.ordinal
            images.size == 2 -> ImageLayoutType.TWO.ordinal
            images.size == 3 -> ImageLayoutType.THREE.ordinal
            images.size == 4 -> ImageLayoutType.FOUR.ordinal
            else -> ImageLayoutType.FIVE_OR_MORE.ordinal
        }

    override fun getItemCount() = 1

    private fun bindSingleImage(
        holder: ImageViewHolder.SingleImageViewHolder,
        position: Int,
    ) {
        Glide.with(holder.itemView.context).load(images[0]).into(holder.image)
    }

    private fun bindTwoImages(
        holder: ImageViewHolder.TwoImagesViewHolder,
        position: Int,
    ) {
        Glide.with(holder.itemView.context).load(images[0]).into(holder.image1)
        Glide.with(holder.itemView.context).load(images[1]).into(holder.image2)
    }

    private fun bindThreeImages(
        holder: ImageViewHolder.ThreeImagesViewHolder,
        position: Int,
    ) {
        Glide.with(holder.itemView.context).load(images[0]).into(holder.image1)
        Glide.with(holder.itemView.context).load(images[1]).into(holder.image2)
        Glide.with(holder.itemView.context).load(images[2]).into(holder.image3)
    }

    private fun bindFourImages(
        holder: ImageViewHolder.FourImagesViewHolder,
        position: Int,
    ) {
        Glide.with(holder.itemView.context).load(images[0]).into(holder.image1)
        Glide.with(holder.itemView.context).load(images[1]).into(holder.image2)
        Glide.with(holder.itemView.context).load(images[2]).into(holder.image3)
        Glide.with(holder.itemView.context).load(images[3]).into(holder.image4)
    }

    private fun bindFiveOrMoreImages(
        holder: ImageViewHolder.FiveOrMoreImagesViewHolder,
        position: Int,
    ) {
        Glide.with(holder.itemView.context).load(images[0]).into(holder.image1)
        Glide.with(holder.itemView.context).load(images[1]).into(holder.image2)
        Glide.with(holder.itemView.context).load(images[2]).into(holder.image3)
        Glide.with(holder.itemView.context).load(images[3]).into(holder.image4)
        Glide.with(holder.itemView.context).load(images[4]).into(holder.image5)

        if (images.size > 5) {
            holder.overlayText.text = "+${images.size - 5}"
        } else {
            holder.overlayText.text = ""
        }
    }
}
