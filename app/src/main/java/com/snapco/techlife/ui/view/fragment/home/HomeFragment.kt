package com.snapco.techlife.ui.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.snapco.techlife.R
import com.snapco.techlife.adapter.home.PostAdapter
import com.snapco.techlife.databinding.FragmentHomeBinding
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.messenger.ChannelActivity
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.home.SharedViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User

class HomeFragment : Fragment(), PostAdapter.OnPostActionListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.btnNextActivityChannel.setOnClickListener{
            startActivity<ChannelActivity>()
        }

        setupRecyclerView()
        observeNewPost()
        return binding.root
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(sharedViewModel.postList.value ?: mutableListOf(), this)
        binding.recyclerViewId.adapter = postAdapter
    }

    private fun observeNewPost() {
        sharedViewModel.postList.observe(viewLifecycleOwner) { postList ->
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
        sharedViewModel.deletePost(sharedViewModel.postList.value?.get(position)?.postId ?: "")
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
        val post = sharedViewModel.postList.value?.get(position)
        val editText = EditText(context).apply { setText(post?.caption) }

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Post")
            .setView(editText)
            .setPositiveButton("Update") { _, _ ->
                val updatedCaption = editText.text.toString()
                val updatedPost = post?.copy(caption = updatedCaption)

                if (updatedPost != null) {
                    sharedViewModel.updatePost(updatedPost)
                    postAdapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePost(position: Int) {
        sharedViewModel.deletePost(sharedViewModel.postList.value?.get(position)?.postId ?: "")
    }

}
