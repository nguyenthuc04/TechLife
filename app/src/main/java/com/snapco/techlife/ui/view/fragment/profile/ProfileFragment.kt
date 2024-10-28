package com.snapco.techlife.ui.view.fragment.profile

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.snapco.techlife.R
import com.snapco.techlife.databinding.FragmentProfileBinding
import com.snapco.techlife.extensions.setupToolbar
import com.snapco.techlife.ui.view.adapter.ProfileTabAdapter

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setupTabs()
    }

    private fun setupTabs() {
        val adapter = ProfileTabAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.icon = getTabIcon(position)
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val fragment = adapter.createFragment(position)
                    if (fragment is LoadableFragment) {
                        fragment.loadData()
                    }
                }
            },
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getTabIcon(position: Int): Drawable? =
        when (position) {
            0 -> requireContext().getDrawable(R.drawable.tab_icon_one_selector)
            1 -> requireContext().getDrawable(R.drawable.tab_icon_two_selector)
            else -> requireContext().getDrawable(R.drawable.tab_icon_three_selector)
        }

    private fun setUpToolbar() {
        setupToolbar(
            toolbar = binding.toolbar,
            text = "thuc.nguyentrung.73997",
            onUser = { handleUserClick() },
            onAddClick = { handleAddClick() },
            onMenuClick = { handleMenuClick() },
        )
    }

    private fun handleMenuClick() {
        Log.d("ProfileFragment", "ok1 ")
    }

    private fun handleAddClick() {
        Log.d("ProfileFragment", "ok1 ")
    }

    private fun handleUserClick() {
        Log.d("ProfileFragment", "ok1 ")
    }
}

interface LoadableFragment {
    fun loadData()
}
