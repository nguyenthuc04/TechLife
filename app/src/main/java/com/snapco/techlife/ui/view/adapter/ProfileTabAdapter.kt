package com.snapco.techlife.ui.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snapco.techlife.ui.view.fragment.profile.ProfileTabOneFragment
import com.snapco.techlife.ui.view.fragment.profile.ProfileTabThreeFragment
import com.snapco.techlife.ui.view.fragment.profile.ProfileTabTwoFragment

class ProfileTabAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> ProfileTabOneFragment()
            1 -> ProfileTabTwoFragment()
            else -> ProfileTabThreeFragment()
        }
}
