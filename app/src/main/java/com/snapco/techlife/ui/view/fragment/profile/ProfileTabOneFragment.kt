package com.snapco.techlife.ui.view.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.snapco.techlife.databinding.FragmentProfileTabOneBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.adapter.PostProfileAdapter
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel

class ProfileTabOneFragment : Fragment() {
    private lateinit var binding: FragmentProfileTabOneBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var postAdapter: PostProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileTabOneBinding.inflate(inflater, container, false)
        setupRecyclerView()
//        UserDataHolder.getUserId()?.let { homeViewModel.getPostsByUser(it) }
        arguments?.getString("user_id")?.let { homeViewModel.getPostsByUser(it) }
        observePosts()
        return binding.root
    }

    private fun setupRecyclerView() {
        postAdapter = PostProfileAdapter(mutableListOf())
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = postAdapter
        }
    }

    private fun observePosts() {
        homeViewModel.postListProfile.observe(viewLifecycleOwner) { postList ->
            Log.d("ProfileTabOneFragment", "Post list observed: $postList")
            if (postList == null || postList.posts.isNullOrEmpty()) {
                binding.noPostsMessage.visibility = View.VISIBLE
                binding.imageView10.visible()
            } else {
                binding.noPostsMessage.visibility = View.GONE
                binding.imageView10.gone()
                postAdapter.updatePosts(postList.posts.reversed())
            }
        }
    }

    companion object {
        fun newInstance(userId: String): ProfileTabOneFragment {
            val fragment = ProfileTabOneFragment()
            val args = Bundle()
            args.putString("user_id", userId)
            fragment.arguments = args
            return fragment
        }
    }
}
