package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Review
import com.snapco.techlife.databinding.ReviewItemBinding
import com.snapco.techlife.extensions.loadImage

class ReviewAdapter(
    private var reviews: List<Review>,
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: ReviewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.reviewStarRating.rating = review.rating
            binding.imgAvatar.loadImage(review.userId.avatar)
            binding.textView37.text = review.userId.name
            binding.textView39.text = review.comment
            binding.textView40.text = review.date
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount() = reviews.size

    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
