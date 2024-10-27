package com.snapco.techlife.ui.view.fragment.course

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snapco.techlife.ui.view.activity.CourseActivity

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyCourseFragment() // Tab đầu tiên
            1 -> OtherCoursesFragment() // Tab thứ hai
            else -> MyCourseFragment()
        }
    }
}
