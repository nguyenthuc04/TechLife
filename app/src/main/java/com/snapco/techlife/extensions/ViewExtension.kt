package com.snapco.techlife.extensions

import android.view.View

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible(): Boolean = visibility == View.VISIBLE

fun View.isGone(): Boolean = visibility == View.GONE

fun View.isInvisible(): Boolean = visibility == View.INVISIBLE
