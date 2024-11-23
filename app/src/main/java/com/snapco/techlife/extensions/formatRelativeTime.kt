package com.snapco.techlife.extensions

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun formatRelativeTime(createdAt: String): String {
    // Chuyển chuỗi thời gian sang đối tượng ZonedDateTime
    val createdTime = ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    // Lấy thời gian hiện tại
    val now = ZonedDateTime.now()

    // Tính khoảng cách thời gian
    val seconds = ChronoUnit.SECONDS.between(createdTime, now)
    val minutes = ChronoUnit.MINUTES.between(createdTime, now)
    val hours = ChronoUnit.HOURS.between(createdTime, now)
    val days = ChronoUnit.DAYS.between(createdTime, now)

    // Định dạng thời gian theo mốc
    return when {
        seconds < 60 -> "$seconds giây trước"
        minutes < 60 -> "$minutes phút trước"
        hours < 24 -> "$hours giờ trước"
        days < 7 -> "$days ngày trước"
        else -> createdTime.toLocalDate().toString() // Hiển thị ngày tháng nếu quá 1 tuần
    }
}
