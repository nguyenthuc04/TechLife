package com.snapco.techlife.extensions

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircleOverlayView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : View(context, attrs, defStyleAttr) {
        private val backgroundPaint =
            Paint().apply {
                color = Color.BLACK
                alpha = 153 // 60% opacity cho nền
                isAntiAlias = true
            }

        private val clearPaint =
            Paint().apply {
                isAntiAlias = true
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) // Xóa hình tròn giữa
            }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val width = width.toFloat()
            val height = height.toFloat()
            val radius = Math.min(width, height) / 2 // Đặt bán kính bằng một nửa chiều rộng hoặc chiều cao

            // Tạo một layer để vẽ
            val saveLayer = canvas.saveLayer(0f, 0f, width, height, null)

            // Vẽ nền màu đen mờ
            canvas.drawRect(0f, 0f, width, height, backgroundPaint)

            // Vẽ hình tròn trong suốt ở giữa
            canvas.drawCircle(width / 2, height / 2, radius, clearPaint)

            // Khôi phục layer
            canvas.restoreToCount(saveLayer)
        }
    }
