package com.snapco.techlife.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.replaceFragment(
    fragment: Fragment,
    addToBackStack: Boolean = true,
) {
    parentFragmentManager.beginTransaction().apply {
        replace(this@replaceFragment.id, fragment)
        if (addToBackStack) addToBackStack(null)
        commit()
    }
}
