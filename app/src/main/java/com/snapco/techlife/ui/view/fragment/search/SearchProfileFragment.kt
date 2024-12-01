// SearchProfileFragment.kt
package com.snapco.techlife.ui.view.fragment.search

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.snapco.techlife.R
import com.snapco.techlife.data.model.FollowRequest
import com.snapco.techlife.data.model.UnfollowRequest
import com.snapco.techlife.databinding.FragmentSearchProfileBinding
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.messenger.ChatActivity
import com.snapco.techlife.ui.view.activity.profile.ReviewActivity
import com.snapco.techlife.ui.view.adapter.ProfileTabAdapter
import com.snapco.techlife.ui.view.fragment.profile.LoadableFragment
import com.snapco.techlife.ui.viewmodel.SearchViewModel
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import com.snapco.techlife.ui.viewmodel.messenger.ChannelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import io.getstream.chat.android.client.ChatClient

class SearchProfileFragment : Fragment() {
    private var _binding: FragmentSearchProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var listChannelViewModel: ChannelViewModel
    private val searchActivityViewModel: SearchViewModel by activityViewModels()
    private val client: ChatClient by lazy { ChatClient.instance() }
    private val userViewModel: UserViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private var isFollowing = false
    private lateinit var currentUserId: String
    private lateinit var profileUserId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchProfileBinding.inflate(inflater, container, false)
        listChannelViewModel = ViewModelProvider(this).get(ChannelViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // Khởi tạo currentUserId
        currentUserId = UserDataHolder.getUserId().toString()

        // Nút quay lại
        binding.toolbarCustomBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Lấy thông tin người dùng và hiển thị
        searchActivityViewModel.user.observe(viewLifecycleOwner) { user ->
            profileUserId = user._id
            setupTabs(user._id)
            homeViewModel.getPostsByUser(user._id)
            binding.toolbarCustomTitle.text = user.name
            binding.txtNichName.text = user.nickname
            Glide.with(binding.imgAvatar.context).load(user.avatar).into(binding.imgAvatar)
            binding.txtTieuSu.text = user.bio
            binding.txtFollower.text = user.followers.size.toString()
            binding.txtFollowing.text = user.following.size.toString()
            binding.txtPost.text = user.posts.size.toString()
            userViewModel.averageRating(user._id)
            binding.imageView15.setOnClickListener {
                val intent = Intent(requireContext(), ReviewActivity::class.java)
                intent.putExtra("USER_ID", user._id)
                startActivity(intent)
            }
            // Kiểm tra trạng thái theo dõi
            isFollowing = user.followers.contains(currentUserId)
            updateFollowButton()

            // Ẩn nút theo dõi và nhắn tin nếu là chính người dùng hiện tại
            if (currentUserId == profileUserId) {
                binding.btnFollow.visibility = View.GONE
                binding.btnMessenger.visibility = View.GONE
            }

            // chuyen sang messenger
            binding.btnMessenger.setOnClickListener {
                val idUserSearch = user._id // id nguoi dc tim kiem
                val idMe = (client.getCurrentUser()?.id ?: "") // id nguoi dung
                val idCheck1 = idUserSearch + idMe
                val idCheck2 = idMe + idUserSearch

                listChannelViewModel.checkChannelExists(idCheck1) { check1 ->
                    if (check1) {
                        // Kênh đã tồn tại với idChannel
                        startActivity<ChatActivity> {
                            putExtra("ID", idCheck1)
                        }
                    } else {
                        // Nếu idChannel không tồn tại, kiểm tra tiếp idCheck
                        listChannelViewModel.checkChannelExists(idCheck2) { check2 ->
                            if (check2) {
                                // Kênh đã tồn tại với idCheck
                                startActivity<ChatActivity> {
                                    putExtra("ID", idCheck2)
                                }
                            } else {
                                // Nếu cả idChannel và idCheck không tồn tại, tạo kênh mới với idChannel
                                createChannel(idCheck2, idUserSearch)
                            }
                        }
                    }
                }
            }
        }
        homeViewModel.postListProfile.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                Log.d("ProfileFragment", "observePosts: ${postList.posts?.size}")
                binding.txtPost.text = it.posts!!.size.toString()
            }
        }
        userViewModel.getAverageRatingResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                binding.starRatingView.rating = response.averageRating
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

    private fun setupTabs(_id: String) {
        val adapter = ProfileTabAdapter(this, _id)
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

    private fun createChannel(
        channelId: String,
        userIdSelect: String,
    ) {
        // Tạo extraData với thuộc tính user1_name và user2_name
        val extraData =
            mapOf(
                "user_create" to client.getCurrentUser()!!.name, // Tên người dùng hiện tại
            )

        // Gọi phương thức createChannel trong ViewModel
        listChannelViewModel.createChannel(channelId, userIdSelect, extraData)
    }

    // Cập nhật giao diện nút theo dõi
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(userId: String): SearchProfileFragment {
            val fragment = SearchProfileFragment()
            val args = Bundle()
            args.putString("user_id", userId)
            fragment.arguments = args
            return fragment
        }
    }
}
