package com.snapco.techlife.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

// Extension cho ImageView để load ảnh với Glide
@BindingAdapter("imageUrl")
fun ImageView.loadImage(
    url: String?,
    placeholderRes: Int = 0,
    errorRes: Int = 0,
) {
    val glideRequest =
        Glide
            .with(this.context)
            .load(url)

    // Set placeholder và error nếu có
    if (placeholderRes != 0) glideRequest.apply(RequestOptions().placeholder(placeholderRes))
    if (errorRes != 0) glideRequest.apply(RequestOptions().error(errorRes))

    glideRequest.into(this)
}
