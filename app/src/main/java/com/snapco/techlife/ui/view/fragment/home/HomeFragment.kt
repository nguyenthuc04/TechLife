package com.snapco.techlife.ui.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.snapco.techlife.R
import com.snapco.techlife.adapter.home.PostAdapter
import com.snapco.techlife.data.model.home.post.Post
import com.snapco.techlife.databinding.FragmentHomeBinding
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel

class HomeFragment : Fragment(), PostAdapter.OnPostActionListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setupRecyclerView()
        observeNewPost()
        return binding.root
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(homeViewModel.postList.value ?: mutableListOf(), this)
        binding.recyclerViewId.adapter = postAdapter
    }

    private fun observeNewPost() {
        homeViewModel.postList.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                postAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPostLongClicked(position: Int) {
        showPostOptionsDialog(position)
    }

    override fun onEditPost(position: Int) {
        showEditPostDialog(position)
    }

    override fun onDeletePost(position: Int) {
        homeViewModel.deletePost(homeViewModel.postList.value?.get(position)?.postId ?: "")
    }

    override fun onLikePost(post: Post, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCommentPost(postId: String) {
        TODO("Not yet implemented")
    }

    private fun showPostOptionsDialog(position: Int) {
        val options = arrayOf("Edit", "Delete")
        AlertDialog.Builder(requireContext())
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditPostDialog(position)
                    1 -> deletePost(position)
                }
            }
            .show()
    }

    private fun showEditPostDialog(position: Int) {
        val post = homeViewModel.postList.value?.get(position)
        val editText = EditText(context).apply { setText(post?.caption) }

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Post")
            .setView(editText)
            .setPositiveButton("Update") { _, _ ->
                val updatedCaption = editText.text.toString()
                val updatedPost = post?.copy(caption = updatedCaption)

                if (updatedPost != null) {
                    homeViewModel.updatePost(updatedPost)
                    postAdapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePost(position: Int) {
        homeViewModel.deletePost(homeViewModel.postList.value?.get(position)?.postId ?: "")
    }
}
