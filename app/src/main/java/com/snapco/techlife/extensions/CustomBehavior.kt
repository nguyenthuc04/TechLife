package com.snapco.techlife.extensions

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout

class CustomBehavior(
    context: Context,
    attrs: AttributeSet,
) : CoordinatorLayout.Behavior<View>(context, attrs) {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View,
    ): Boolean = dependency is EditText

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View,
    ): Boolean {
        val rect = Rect()
        parent.getWindowVisibleDisplayFrame(rect)
        val screenHeight = parent.height
        val keypadHeight = screenHeight - rect.bottom

        if (keypadHeight > 0) {
            child.translationY = -keypadHeight.toFloat() // Di chuyển view lên trên bàn phím
        } else {
            child.translationY = 0f // Đặt lại vị trí ban đầu
        }

        return true
    }
}
