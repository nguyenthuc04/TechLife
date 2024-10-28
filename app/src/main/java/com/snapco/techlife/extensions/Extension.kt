package com.snapco.techlife.extensions

import android.content.Context
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

fun Fragment.setupToolbar(
    toolbar: Toolbar,
    text: String? = null,
    onUser: (() -> Unit)? = null,
    onAddClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
) {
    (activity as? AppCompatActivity)?.let { appCompatActivity ->
        appCompatActivity.setSupportActionBar(toolbar)

        val textView = toolbar.findViewById<TextView>(R.id.toolbar_custom_title)
        textView?.text = text
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
