package com.snapco.techlife.extensions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R

sealed class ImageViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {
    class SingleImageViewHolder(
        itemView: View,
    ) : ImageViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.singleImage)
    }

    class TwoImagesViewHolder(
        itemView: View,
    ) : ImageViewHolder(itemView) {
        val image1: ImageView = itemView.findViewById(R.id.image1)
        val image2: ImageView = itemView.findViewById(R.id.image2)
    }

    class ThreeImagesViewHolder(
        itemView: View,
    ) : ImageViewHolder(itemView) {
        val image1: ImageView = itemView.findViewById(R.id.image1)
        val image2: ImageView = itemView.findViewById(R.id.image2)
        val image3: ImageView = itemView.findViewById(R.id.image3)
    }

    class FourImagesViewHolder(
        itemView: View,
    ) : ImageViewHolder(itemView) {
        val image1: ImageView = itemView.findViewById(R.id.image1)
        val image2: ImageView = itemView.findViewById(R.id.image2)
        val image3: ImageView = itemView.findViewById(R.id.image3)
        val image4: ImageView = itemView.findViewById(R.id.image4)
    }

    class FiveOrMoreImagesViewHolder(
        itemView: View,
    ) : ImageViewHolder(itemView) {
        val image1: ImageView = itemView.findViewById(R.id.image1)
        val image2: ImageView = itemView.findViewById(R.id.image2)
        val image3: ImageView = itemView.findViewById(R.id.image3)
        val image4: ImageView = itemView.findViewById(R.id.image4)
        val image5: ImageView = itemView.findViewById(R.id.image5)
        val overlayText: TextView = itemView.findViewById(R.id.overlayText)
    }
}
