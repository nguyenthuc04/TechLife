package com.snapco.techlife.extensions

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
