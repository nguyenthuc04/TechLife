package com.snapco.techlife.ui.view.fragment.home

import ChatViewModel
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
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.events.NotificationMessageNewEvent

class HomeFragment :
    Fragment(),
    PostAdapter.OnPostActionListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val channelViewModel : ChannelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        channelViewModel.getListChannel(UserDataHolder.getUserId().toString())
        chatViewModel.unreadMessagesCount.observe(viewLifecycleOwner) { unreadCount ->
            binding.txtUnread.text = unreadCount.toString()
            if(unreadCount > 0) {
                binding.txtUnread.visible()
            } else {
                binding.txtUnread.gone()
            }
        }

        binding.btnNextActivityChannel.setOnClickListener {
            startActivity<ChannelActivity>()
        }
        binding.btnNotification.setOnClickListener{
            startActivity<NotificationActivity>()
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
            Log.d("HomeFragment", "observePosts: $postList")
            postList?.let {
                postAdapter.updatePosts(it.reversed())
            }
        }
    }

    override fun onPostLongClicked(position: Int) {
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

    override fun onLikePost(
        post: Post,
        position: Int,
    ) {
        postAdapter.updateLikeButtonAt(position)
        // Gọi ViewModel để cập nhật API
        UserDataHolder.getUserId()?.let { homeViewModel.likePost(post._id, it) }
    }

    override fun onCommentPost(postId: String) {
        val bottomSheet = BottomSheetCommentFragment.newInstance(postId)
        bottomSheet.show(parentFragmentManager, BottomSheetCommentFragment::class.java.simpleName)
    }
}
