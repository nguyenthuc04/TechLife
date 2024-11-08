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
import com.google.android.material.tabs.TabLayoutMediator
import com.snapco.techlife.R
import com.snapco.techlife.data.model.GetUserResponse
import com.snapco.techlife.databinding.FragmentProfileBinding
import com.snapco.techlife.extensions.*
import com.snapco.techlife.ui.view.activity.profile.EditProfileActivity
import com.snapco.techlife.ui.view.adapter.ProfileTabAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetProfileAddFragment
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentProfileBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarActions()
        setupTabs()
        observeUserData()
        setupSwipeRefresh()
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            updateUI(response)
        }
        binding.button.setOnClickListener { startActivity<EditProfileActivity>() }
    }

    private fun setupToolbarActions() {
        setupClickToolbar(
            toolbar = binding.toolbar,
            onUser = { logAction("User clicked") },
            onAddClick = { BottomSheetProfileAddFragment().show(parentFragmentManager, null) },
            onMenuClick = { logAction("Menu clicked") },
        )
    }

    private fun setupTabs() {
        val adapter = ProfileTabAdapter(this)
        binding.viewPager.apply {
            this.adapter = adapter
            offscreenPageLimit = 2
            registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        (adapter.createFragment(position) as? LoadableFragment)?.loadData()
                    }
                },
            )
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.icon = getTabIcon(position)
        }.attach()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getTabIcon(position: Int): Drawable? =
        requireContext().getDrawable(
            when (position) {
                0 -> R.drawable.tab_icon_one_selector
                1 -> R.drawable.tab_icon_two_selector
                else -> R.drawable.tab_icon_three_selector
            },
        )

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener { loadData() }
    }

    private fun observeUserData() {
        UserDataHolder.getUserId()?.let { userViewModel.getUser(it) }
        userViewModel.userResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                GetUserResponseHolder.setGetUserResponse(it)
                updateUI(it)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        UserDataHolder.getUserId()?.let { userViewModel.getUser(it) }
    }

    private fun updateUI(response: GetUserResponse) =
        binding.apply {
            setupTextToolbar(toolbar, response.user.name)
            textView6.text = response.followersCount.toString()
            textView15.text = response.followingCount.toString()
            textView4.text = response.postsCount.toString()
            textView17.text = response.user.nickname
            textView18.text = response.user.bio.takeIf { it != "null" } ?: ""
            imgAvatar.loadImage(response.user.avatar)
        }

    private fun logAction(message: String) = Log.d("ProfileFragment", message)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface LoadableFragment {
    fun loadData()
}
