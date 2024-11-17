package com.snapco.techlife.extensions

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.snapco.techlife.R

fun Context.showToast(
    message: CharSequence,
    duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.setupClickToolbar(
    toolbar: Toolbar,
    onUser: (() -> Unit)? = null,
    onAddClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
) {
    (activity as? AppCompatActivity)?.let { appCompatActivity ->
        appCompatActivity.setSupportActionBar(toolbar)
        toolbar.findViewById<View>(R.id.linearLayout7)?.setOnClickListener {
            onUser?.invoke()
        }

        toolbar.findViewById<View>(R.id.toolbar_add)?.setOnClickListener {
            onAddClick?.invoke()
        }

        toolbar.findViewById<View>(R.id.toolbar_menu)?.setOnClickListener {
            onMenuClick?.invoke()
        }
    }
}

fun Fragment.setupTextToolbar(
    toolbar: Toolbar,
    text: String? = null,
) {
    (activity as? AppCompatActivity)?.let { appCompatActivity ->
        appCompatActivity.setSupportActionBar(toolbar)

        val textView = toolbar.findViewById<TextView>(R.id.toolbar_custom_title)
        textView?.text = text
    }
}

fun Context.sharedPreferences(name: String = "${packageName}_default_prefs"): SharedPreferences =
    getSharedPreferences(name, Context.MODE_PRIVATE)

fun SharedPreferences.put(
    key: String,
    value: Any,
) {
    with(edit()) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }
        apply()
    }
}

// Hàm mở rộng để lấy giá trị từ SharedPreferences
fun <T> SharedPreferences.get(
    key: String,
    default: T,
): T =
    when (default) {
        is String -> getString(key, default) as T
        is Int -> getInt(key, default) as T
        is Boolean -> getBoolean(key, default) as T
        is Float -> getFloat(key, default) as T
        is Long -> getLong(key, default) as T
        else -> throw IllegalArgumentException("Unsupported type")
    }
