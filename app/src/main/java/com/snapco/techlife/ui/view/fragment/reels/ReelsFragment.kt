package com.snapco.techlife.ui.view.fragment.reels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.snapco.techlife.R
import com.snapco.techlife.data.model.LikeReelNotificationRequest
import com.snapco.techlife.data.model.Reel
import com.snapco.techlife.databinding.FragmentReelsBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.adapter.ReelAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetCommentReelFragment
import com.snapco.techlife.ui.viewmodel.ReelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReelsFragment :
    Fragment(),
    ReelAdapter.OnReelActionListener {
    private lateinit var binding: FragmentReelsBinding
    private lateinit var reelAdapter: ReelAdapter
    private val reelViewModel: ReelViewModel by viewModels()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reels, container, false)
        shimmerFrameLayout = binding.shimmerViewContainer
        setupViewPager()
        observeReels()
        return binding.root
    }

    private fun observeReels() {
        lifecycleScope.launch {
            reelViewModel.getReels().collectLatest { pagingData ->
                Log.d("ReelsFragment", "Received paging data")
                reelAdapter.submitData(pagingData)
            }
        }

        reelAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                shimmerFrameLayout.startShimmer()
                shimmerFrameLayout.visible()
                binding.viewPager.gone()
            } else {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.gone()
                binding.viewPager.visible()
            }
        }
    }

    private fun setupViewPager() {
        reelAdapter = ReelAdapter(this, binding.viewPager.getChildAt(0) as RecyclerView)
        binding.viewPager.adapter = reelAdapter
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    reelAdapter.loadVideoAtPosition(position)
                }
            },
        )
    }

    override fun onPostLongClicked(position: Int) {
        // Implement your logic here
    }

    override fun onEditPost(position: Int) {
        // Implement your logic here
    }

    override fun onDeletePost(position: Int) {
        // Implement your logic here
    }

    override fun onLikePost(
        post: Reel,
        position: Int,
    ) {
        val likeRequest =
            LikeReelNotificationRequest(
                userId = UserDataHolder.getUserId().toString(),
                yourID = post.userId,
                nameUser = UserDataHolder.getUserName().toString(),
                imgUser = UserDataHolder.getUserAvatar().toString(),
            )
        reelViewModel.likeReel(post._id, likeRequest)
        refreshCurrentReel(position)
    }

    private fun refreshCurrentReel(position: Int) {
        val currentReel = reelAdapter.snapshot().items[position]
        currentReel?.let {
            reelViewModel.likeReelResponse.observe(viewLifecycleOwner) { response ->
                Log.d("ReelsFragment", "Like response: $response")
                if (response.success) {
                    val updatedReel = response.reel
                    updatedReel?.let { reel ->
                        val viewHolder =
                            (binding.viewPager.getChildAt(0) as RecyclerView)
                                .findViewHolderForAdapterPosition(position) as? ReelAdapter.ViewHolder
                        viewHolder?.updateCommentsAndLikes(reel)
                    }
                }
            }
        }
    }

    override fun onCommentPost(
        postId: String,
        position: Int,
        userId: String,
    ) {
        val bottomSheet = BottomSheetCommentReelFragment.newInstance(postId, userId)
        bottomSheet.show(
            parentFragmentManager,
            BottomSheetCommentReelFragment::class.java.simpleName,
        )
    }
}
