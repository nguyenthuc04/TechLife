package com.snapco.techlife.ui.view.fragment.search

import ChatViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.FragmentHomeBinding
import com.snapco.techlife.databinding.FragmentPostDetailBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.activity.NotificationActivity
import com.snapco.techlife.ui.view.activity.messenger.ChannelActivity
import com.snapco.techlife.ui.view.adapter.PostAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetCommentFragment
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import com.snapco.techlife.ui.viewmodel.messenger.ChannelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class PostDetailFragment : Fragment(), PostAdapter.OnPostActionListener {
    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var postAdapter: PostAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var postId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed() // Quay lại fragment trước đó
        }
        arguments?.let {
            postId = it.getString("postId")
        }
        setupRecyclerView()
        homeViewModel.getListPosts()
        observePosts()
        return binding.root
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(mutableListOf(), this) // 'this' ở đây chính là PostDetailFragment
        binding.recyclerViewId.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }
    }

    private fun observePosts() {
        homeViewModel.posts.observe(viewLifecycleOwner) { postList ->
            Log.d("PostDetail", "observePosts: $postList")
            postList?.let {
                val selectedPost = postList.find { post -> post._id == postId }
                selectedPost?.let {
                    postAdapter.updatePosts(listOf(it))  // Hiển thị chỉ bài viết này
                }

            }
        }
    }

    // Implement các phương thức của OnPostActionListener
    override fun onPostLongClicked(position: Int) {
        // Xử lý long-click tại đây
    }

    override fun onEditPost(position: Int) {
        // Xử lý sự kiện edit tại đây
    }

    override fun onDeletePost(position: Int) {
        // Xử lý sự kiện delete tại đây
    }

    override fun onLikePost(post: Post, position: Int) {
        postAdapter.updateLikeButtonAt(position)
        // Gọi ViewModel để cập nhật API
        UserDataHolder.getUserId()?.let { homeViewModel.likePost(post._id, it) }
    }

    override fun onCommentPost(postId: String) {
        val bottomSheet = BottomSheetCommentFragment.newInstance(postId)
        bottomSheet.show(parentFragmentManager, BottomSheetCommentFragment::class.java.simpleName)
    }
}
