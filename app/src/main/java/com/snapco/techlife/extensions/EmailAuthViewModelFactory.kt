package com.snapco.techlife.extensions

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snapco.techlife.ui.viewmodel.EmailAuthViewModel

class EmailAuthViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailAuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmailAuthViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
