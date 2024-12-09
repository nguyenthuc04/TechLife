package com.snapco.techlife.ui.view.fragment.course

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.snapco.techlife.R
import com.snapco.techlife.data.model.FollowRequest
import com.snapco.techlife.data.model.UnfollowRequest
import com.snapco.techlife.databinding.FragmentCourseStudentsBinding
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.messenger.ChatActivity
import com.snapco.techlife.ui.view.adapter.ProfileTabAdapter
import com.snapco.techlife.ui.view.fragment.profile.LoadableFragment
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import com.snapco.techlife.ui.viewmodel.messenger.ChannelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import io.getstream.chat.android.client.ChatClient

class CourseStudentsFragment : Fragment() {
    private lateinit var binding: FragmentCourseStudentsBinding
    private val userViewModel: UserViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private var isFollowing = false
    private lateinit var currentUserId: String
    private lateinit var profileUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCourseStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = UserDataHolder.getUserId().toString()

        binding.toolbarCustomBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val userId = arguments?.getString("userId") ?: return
        Log.d("CourseStudentsFragment", "Received User ID: $userId")
        userViewModel.getCourseByUser(userId)
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                setupTabs(userId)
                Log.d("CourseStudentsFragment", "User data: $user")
                homeViewModel.getPostsByUser(userId)
                profileUserId = userId
                binding.toolbarCustomTitle.text = user.name
                binding.txtNichName.text = user.nickname
                Glide.with(binding.imgAvatar.context).load(user.avatar).into(binding.imgAvatar)
                binding.txtTieuSu.text = user.bio
                binding.txtFollower.text = user.followers.size.toString()
                binding.txtFollowing.text = user.following.size.toString()
                binding.txtPost.text = user.posts.size.toString()
            } else {
                Log.e("CourseStudentsFragment", "User data is null")
            }

            // Kiểm tra trạng thái theo dõi
            isFollowing = user.followers.contains(currentUserId)
            updateFollowButton()

            // Ẩn nút theo dõi và nhắn tin nếu là chính người dùng hiện tại
            if (currentUserId == profileUserId) {
                binding.btnFollow.visibility = View.GONE
                binding.btnMessenger.visibility = View.GONE
            }

        }

        homeViewModel.postListProfile.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                Log.d("ProfileFragment", "observePosts: ${postList.posts?.size}")
                binding.txtPost.text = it.posts!!.size.toString()
            }
        }

        // Xử lý nút theo dõi/bỏ theo dõi
        binding.btnFollow.setOnClickListener {
            if (isFollowing) {
                userViewModel.unfollowUser(UnfollowRequest(currentUserId, profileUserId))
            } else {
                userViewModel.followUser(FollowRequest(currentUserId, profileUserId))
            }
        }

        // Lắng nghe kết quả theo dõi và cập nhật giao diện
        userViewModel.followResponse.observe(viewLifecycleOwner) { response ->
            if (response!!.success) {
                isFollowing = true
                updateFollowButton()
                binding.txtFollower.text =
                    (
                            binding.txtFollower.text
                                .toString()
                                .toInt() + 1
                            ).toString()
                Toast.makeText(requireContext(), "Đã theo dõi", Toast.LENGTH_SHORT).show()
            }
        }

        // Lắng nghe kết quả bỏ theo dõi và cập nhật giao diện
        userViewModel.unfollowResponse.observe(viewLifecycleOwner) { response ->
            if (response!!.success) {
                isFollowing = false
                updateFollowButton()
                binding.txtFollower.text =
                    (
                            binding.txtFollower.text
                                .toString()
                                .toInt() - 1
                            ).toString()
                Toast.makeText(requireContext(), "Đã bỏ theo dõi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupTabs(id: String) {
        val adapter = ProfileTabAdapter(this, id)
        binding.viewPager.apply {
            this.adapter = adapter
            offscreenPageLimit = 1
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
                else -> R.drawable.tab_icon_two_selector
            },
        )

    private fun updateFollowButton() {
        if (isFollowing) {
            binding.btnFollow.text = "Bỏ theo dõi"
            binding.btnFollow.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_gray,
                ),
            )
        } else {
            binding.btnFollow.text = "Theo dõi"
            binding.btnFollow.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blueLogo,
                ),
            )
        }
    }

    companion object {
        fun newInstance(userId: String): CourseStudentsFragment {
            val fragment = CourseStudentsFragment()
            val args = Bundle()
            args.putString("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }
}