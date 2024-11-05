package com.snapco.techlife.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import com.snapco.techlife.R

@SuppressLint("ResourceType")
fun showCustomAlertDialog(
    activity: Activity,
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit,
) {
    val builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)

    builder
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText) { dialog, which ->
            positiveAction() // Thực thi hành động khi nhấn nút Positive.
            dialog.dismiss()
        }.setNegativeButton(negativeButtonText) { dialog, which ->
            negativeAction() // Thực thi hành động khi nhấn nút Negative.
            dialog.dismiss()
        }

    // Tạo và hiển thị dialog.
    val dialog = builder.create()
    dialog.show()
}

@SuppressLint("ResourceType")
fun showCustomAlertDialogINeedAcc(
    activity: Activity,
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit,
) {
    val builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)

    builder
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText) { dialog, which ->
            positiveAction() // Thực thi hành động khi nhấn nút Positive.
            dialog.dismiss()
        }.setNegativeButton(negativeButtonText) { dialog, which ->
            negativeAction() // Thực thi hành động khi nhấn nút Negative.
            dialog.dismiss()
        }

    // Tạo và hiển thị dialog.
    val dialog = builder.create()
    dialog.show()
}
