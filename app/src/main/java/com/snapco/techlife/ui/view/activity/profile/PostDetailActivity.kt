package com.snapco.techlife.ui.view.activity.profile

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.data.model.LikeNotificationRequest
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.ActivityPostDetailBinding
import com.snapco.techlife.ui.view.adapter.PostAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetCommentFragment
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class PostDetailActivity :
    AppCompatActivity(),
    PostAdapter.OnPostActionListener {
    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var postAdapter: PostAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Quay lại fragment trước đó
        }
        val postId = intent.getStringExtra("postId")
        setupRecyclerView()
        homeViewModel.getListPosts()
        observePosts(postId)
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(mutableListOf(), this) // 'this' ở đây chính là PostDetailFragment
        binding.recyclerViewId.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }
    }

    private fun observePosts(postId: String?) {
        homeViewModel.posts.observe(this) { postList ->
            Log.d("PostDetail", "observePosts: $postList")
            postList?.let {
                val selectedPost = postList.find { post -> post._id == postId }
                selectedPost?.let {
                    postAdapter.updatePosts(listOf(it)) // Hiển thị chỉ bài viết này
                }
            }
        }
    }

    override fun onPostLongClicked(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onEditPost(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onDeletePost(position: Int) {
        TODO("Not yet implemented")
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
        bottomSheet.show(supportFragmentManager, BottomSheetCommentFragment::class.java.simpleName)
    }
}
