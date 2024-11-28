package com.snapco.techlife.ui.viewmodel.objectdataholder

import android.content.Context
import android.content.SharedPreferences

object DataCheckMart {
    private const val PREF_NAME = "MyChecked" // Tên file SharedPreferences

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getMultiplePreferences(context: Context, key: String): Set<String> {
        val sharedPreferences = getPreferences(context)
        return sharedPreferences.getStringSet(key, emptySet()) ?: emptySet()
    }

    fun saveMultiplePreferences(context: Context, key: String, values: Set<String>) {
        val sharedPreferences = getPreferences(context)
        with(sharedPreferences.edit()) {
            putStringSet(key, values)
            apply()
        }
    }
}