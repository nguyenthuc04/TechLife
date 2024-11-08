package com.snapco.techlife.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView

fun String.isEmailValid(): Boolean =
    android.util.Patterns.EMAIL_ADDRESS
        .matcher(this)
        .matches()

fun String.isValidPassword(): Boolean {
    val pattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}\$")
    return pattern.matches(this)
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
