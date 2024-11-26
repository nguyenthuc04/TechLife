package com.snapco.techlife.ui.view.fragment.home

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
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.FragmentHomeBinding
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.home.EditPostActivity
import com.snapco.techlife.ui.view.activity.messenger.ChannelActivity
import com.snapco.techlife.ui.view.adapter.PostAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetCommentFragment
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class HomeFragment : Fragment(), PostAdapter.OnPostActionListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.btnNextActivityChannel.setOnClickListener {
            startActivity<ChannelActivity>()
        }

        setupRecyclerView()
        homeViewModel.getListPosts()
        observePosts()

        return binding.root
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(mutableListOf(), this)
        binding.recyclerViewId.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }
    }

    private fun observePosts() {
        homeViewModel.posts.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                postAdapter.updatePosts(it.reversed())
            }
        }
    }

    override fun onPostLongClicked(position: Int) {
        // Handle long click if required
        Log.d("HomeFragment", "Post long clicked at position: $position")
    }

    override fun onEditPost(position: Int) {
        val post = postAdapter.modelList[position]
        Log.d("HomeFragment", "Editing post: $post")

        // Navigate to EditPostActivity, passing the post details
        val context = requireContext()
        context.startActivity<EditPostActivity> {
            putExtra("POST_ID", post._id)
            putExtra("USER_NAME", post.userName)
            putExtra("CAPTION", post.caption)
            putExtra("USER_IMAGE_URL", post.userImageUrl)
            putStringArrayListExtra("IMAGES", ArrayList(post.imageUrl))
        }
    }

    override fun onDeletePost(position: Int) {
        val post = postAdapter.modelList[position]
        // Remove post from adapter
        postAdapter.modelList.removeAt(position)
        postAdapter.notifyItemRemoved(position)
        // Perform API call to delete post
        homeViewModel.deletePost(post._id)
    }

    override fun onLikePost(post: Post, position: Int) {
        postAdapter.updateLikeButtonAt(position)
        // Call ViewModel to update like in API
        UserDataHolder.getUserId()?.let { homeViewModel.likePost(post._id, it) }
        Log.d("HomeFragment", "Post liked at position: $position, Post: $post")
    }

    override fun onCommentPost(postId: String) {
        val bottomSheet = BottomSheetCommentFragment.newInstance(postId)
        bottomSheet.show(parentFragmentManager, BottomSheetCommentFragment::class.java.simpleName)
        Log.d("HomeFragment", "Opening comments for post ID: $postId")
    }
}