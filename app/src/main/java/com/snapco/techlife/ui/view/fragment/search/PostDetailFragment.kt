package com.snapco.techlife.ui.view.fragment.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.data.model.LikeNotificationRequest
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.FragmentPostDetailBinding
import com.snapco.techlife.ui.view.adapter.PostAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetCommentFragment
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class PostDetailFragment :
    Fragment(),
    PostAdapter.OnPostActionListener {
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
                    postAdapter.updatePosts(listOf(it)) // Hiển thị chỉ bài viết này
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

    override fun onLikePost(
        post: Post,
        position: Int,
    ) {
        postAdapter.updateLikeButtonAt(position)
        val likeRequest =
            LikeNotificationRequest(
                userId = UserDataHolder.getUserId().toString(),
                yourID = post.userId,
                nameUser = UserDataHolder.getUserName().toString(),
                imgUser = UserDataHolder.getUserAvatar().toString(),
            )
        homeViewModel.likePost(post._id, likeRequest)
    }

    override fun onCommentPost(
        postId: String,
        userId: String,
    ) {
        val bottomSheet = BottomSheetCommentFragment.newInstance(postId, userId)
        bottomSheet.show(parentFragmentManager, BottomSheetCommentFragment::class.java.simpleName)
    }
}
