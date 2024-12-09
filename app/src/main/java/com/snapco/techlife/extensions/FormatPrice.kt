package com.snapco.techlife.extensions

import java.text.NumberFormat
import java.util.Locale

fun Int.formatPrice(): String {
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    return "${formatter.format(this)}đ"
}