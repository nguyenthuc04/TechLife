package com.snapco.techlife.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView

fun String.isValidVietnamesePhoneNumber(): Boolean {
    val pattern = Regex("^(\\+84|84|0)(3|5|7|8|9)[0-9]{8}\$")
    return pattern.matches(this)
}

fun String.isValidEmail(): Boolean {
    val pattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
    return pattern.matches(this)
}

fun String.isValidPassword(): Boolean {
    val pattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}\$")
    return pattern.matches(this)
}

fun String.formatToE164(): String =
    if (startsWith("0")) {
        "+84${substring(1)}"
    } else if (startsWith("+84")) {
        this // Đã có mã quốc gia Việt Nam
    } else {
        "+84$this" // Thêm mã quốc gia nếu không có
    }

fun Any.getTag(): String = this::class.java.simpleName

fun TextView.setHighlightedText(
    fullText: String,
    highlights: List<Pair<String, Int>>,
) {
    val spannableString = SpannableString(fullText)
    highlights.forEach { (text, color) ->
        val start = fullText.indexOf(text)
        if (start != -1) {
            spannableString.setSpan(
                ForegroundColorSpan(color),
                start,
                start + text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
    }
    this.text = spannableString
}
