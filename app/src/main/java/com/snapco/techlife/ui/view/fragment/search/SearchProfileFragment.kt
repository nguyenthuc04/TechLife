// SearchProfileFragment.kt
package com.snapco.techlife.ui.view.fragment.search

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.snapco.techlife.R
import com.snapco.techlife.data.model.FollowRequest
import com.snapco.techlife.data.model.UnfollowRequest
import com.snapco.techlife.databinding.FragmentSearchProfileBinding import com.snapco.techlife.ui.view.fragment.profile.LoadableFragment
import com.snapco.techlife.ui.viewmodel.FollowViewModel
import com.snapco.techlife.ui.viewmodel.SearchViewModel
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.data.model.follow.FollowRepository
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class SearchProfileFragment : Fragment() {
    private var _binding: FragmentSearchProfileBinding? = null
    private val binding get() = _binding!!
    private val searchActivityViewModel: SearchViewModel by activityViewModels()
    private val followViewModel: FollowViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = FollowRepository() // Khởi tạo FollowRepository
                return FollowViewModel(repository) as T // Tạo FollowViewModel với repository
            }
        }
    }

    private var isFollowing = false
    private lateinit var currentUserId: String
    private lateinit var profileUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            binding.toolbarCustomTitle.text = user.name
            binding.txtNichName.text = user.nickname
            Glide.with(binding.imgAvatar.context).load(user.avatar).into(binding.imgAvatar)
            binding.txtTieuSu.text = user.bio
            binding.txtFollower.text = user.followers.size.toString()
            binding.txtFollowing.text = user.following.size.toString()
            binding.txtPost.text = user.posts.size.toString()

            // Kiểm tra trạng thái theo dõi
            isFollowing = user.followers.contains(currentUserId)
            updateFollowButton()

            // Ẩn nút theo dõi và nhắn tin nếu là chính người dùng hiện tại
            if (currentUserId == profileUserId) {
                binding.btnFollow.visibility = View.GONE
                binding.btnMessenger.visibility = View.GONE
            }
        }

        // Xử lý nút theo dõi/bỏ theo dõi
        binding.btnFollow.setOnClickListener {
            if (isFollowing) {
                followViewModel.unfollowUser(UnfollowRequest(currentUserId, profileUserId))
            } else {
                followViewModel.followUser(FollowRequest(currentUserId, profileUserId))
            }
        }

        // Lắng nghe kết quả theo dõi và cập nhật giao diện
        followViewModel.followResponse.observe(viewLifecycleOwner) { response ->
            if (response.success) {
                isFollowing = true
                updateFollowButton()
                binding.txtFollower.text =
                    (binding.txtFollower.text.toString().toInt() + 1).toString()
                Toast.makeText(requireContext(), "Đã theo dõi", Toast.LENGTH_SHORT).show()
            }
        }

        // Lắng nghe kết quả bỏ theo dõi và cập nhật giao diện
        followViewModel.unfollowResponse.observe(viewLifecycleOwner) { response ->
            if (response.success) {
                isFollowing = false
                updateFollowButton()
                binding.txtFollower.text =
                    (binding.txtFollower.text.toString().toInt() - 1).toString()
                Toast.makeText(requireContext(), "Đã bỏ theo dõi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Cập nhật giao diện nút theo dõi
    private fun updateFollowButton() {
        if (isFollowing) {
            binding.btnFollow.text = "Bỏ theo dõi"
            binding.btnFollow.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_gray
                )
            )
        } else {
            binding.btnFollow.text = "Theo dõi"
            binding.btnFollow.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blueLogo
                )
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}