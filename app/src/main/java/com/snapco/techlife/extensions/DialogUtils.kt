package com.snapco.snaplife.extensions

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.snapco.snaplife.R

fun showCustomAlertDialog(
    activity: Activity,
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit,
) {
    val dialogView = LayoutInflater.from(activity).inflate(R.layout.custom_alert_dialog, null)

    val dialog =
        AlertDialog
            .Builder(activity)
            .setView(dialogView)
            .create()

    val titleTextView = dialogView.findViewById<TextView>(R.id.dialogTitle)
    val messageTextView = dialogView.findViewById<TextView>(R.id.dialogMessage)
    val positiveButton = dialogView.findViewById<Button>(R.id.positiveButton)
    val negativeButton = dialogView.findViewById<Button>(R.id.negativeButton)

    titleTextView.text = title
    messageTextView.text = message
    positiveButton.text = positiveButtonText
    negativeButton.text = negativeButtonText

    positiveButton.setOnClickListener {
        positiveAction()
        dialog.dismiss()
    }

    negativeButton.setOnClickListener {
        negativeAction()
        dialog.dismiss()
    }

    dialog.show()
}
