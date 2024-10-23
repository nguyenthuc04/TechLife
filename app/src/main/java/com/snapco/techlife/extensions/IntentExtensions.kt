package com.snapco.snaplife.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle

// Extension để khởi động một Activity mới
inline fun <reified T : Any> Context.startActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {},
) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent, options)
}

// Extension để khởi động một Broadcast
inline fun <reified T : Any> Context.sendBroadcast(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init()
    sendBroadcast(intent)
}

// Extension để lấy dữ liệu từ Intent với giá trị mặc định
//  val value: String? = intent.getExtra("key", "default value")
//        val intValue: Int = intent.getExtra("intKey", 0) ?: 0
inline fun <reified T : Any> Intent.getExtra(
    key: String,
    defaultValue: T? = null,
): T? =
    when (T::class) {
        String::class -> getStringExtra(key) as T?
        Int::class -> getIntExtra(key, defaultValue as? Int ?: -1) as T?
        Boolean::class -> getBooleanExtra(key, defaultValue as? Boolean ?: false) as T?
        Double::class -> getDoubleExtra(key, defaultValue as? Double ?: -1.0) as T?
        Float::class -> getFloatExtra(key, defaultValue as? Float ?: -1.0f) as T?
        Long::class -> getLongExtra(key, defaultValue as? Long ?: -1L) as T?
        else -> defaultValue
    }

// Extension để khởi động một Service mới
inline fun <reified T : Any> Context.startService(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startService(intent)
}
