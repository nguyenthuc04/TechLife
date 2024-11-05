package com.snapco.techlife.ui.view.fragment.profile

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.snapco.techlife.R
import com.snapco.techlife.databinding.FragmentProfileBinding
import com.snapco.techlife.extensions.setupClickToolbar
import com.snapco.techlife.extensions.setupTextToolbar
import com.snapco.techlife.ui.view.adapter.ProfileTabAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetProfileAddFragment
import com.snapco.techlife.ui.viewmodel.UserDataHolder
import com.snapco.techlife.ui.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by viewModels()

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
        setUpClickToolbar()
        setupTabs()
        UserDataHolder.getUserId()?.let { userId ->
            userViewModel.getUser(userId)
        }
        userViewModel.userResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                setUpToolbar(response.user.name)
                binding.textView6.text = response.followersCount.toString()
                binding.textView15.text = response.followingCount.toString()
                binding.textView4.text = response.postsCount.toString()
                binding.textView17.text = response.user.nickname
                binding.textView18.text = if (response.user.bio == "null") "" else response.user.bio
                Glide
                    .with(this)
                    .load(response.user.avatar)
                    .into(binding.imgAvatar)
            }
        }
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

    private fun setUpToolbar(name: String) {
        setupTextToolbar(
            toolbar = binding.toolbar,
            text = name,
        )
    }

    private fun setUpClickToolbar() {
        setupClickToolbar(
            toolbar = binding.toolbar,
            onUser = { handleUserClick() },
            onAddClick = { handleAddClick() },
            onMenuClick = { handleMenuClick() },
        )
    }

    private fun handleMenuClick() {
        Log.d("ProfileFragment", "ok1 ")
    }

    private fun handleAddClick() {
        val bottomsheetprofile = BottomSheetProfileAddFragment()
        bottomsheetprofile.show(parentFragmentManager, bottomsheetprofile.tag)
    }

    private fun handleUserClick() {
        Log.d("ProfileFragment", "ok1 ")
    }
}

interface LoadableFragment {
    fun loadData()
}
