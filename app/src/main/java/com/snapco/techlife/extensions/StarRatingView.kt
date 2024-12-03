package com.snapco.techlife.extensions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.snapco.techlife.R

class StarRatingView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : LinearLayout(context, attrs, defStyleAttr) {
        private val stars = mutableListOf<ImageView>()
        var rating: Int = 0
            set(value) {
                field = value
                updateStars()
            }

        init {
            orientation = HORIZONTAL
            for (i in 1..5) {
                val star = LayoutInflater.from(context).inflate(R.layout.start_item, this, false) as ImageView
                star.setOnClickListener { rating = i }
                addView(star)
                stars.add(star)
            }
        }

        private fun updateStars() {
            stars.forEachIndexed { index, star ->
                star.setImageResource(if (index < rating) R.drawable.ic_star else R.drawable.ic_star_empty)
            }
        }
    }
