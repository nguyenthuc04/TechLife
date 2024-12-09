package com.snapco.techlife.ui.view.activity.profile

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.snapco.techlife.R
import com.snapco.techlife.data.model.LikeReelNotificationRequest
import com.snapco.techlife.data.model.Reel
import com.snapco.techlife.databinding.ActivityReelDetailBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.adapter.ReelAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetCommentReelFragment
import com.snapco.techlife.ui.view.fragment.reels.ReelDetailFragment
import com.snapco.techlife.ui.viewmodel.ReelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReelDetailActivity :
    AppCompatActivity(),
    ReelAdapter.OnReelActionListener {
    private lateinit var binding: ActivityReelDetailBinding
    private lateinit var reelAdapter: ReelAdapter
    private val reelViewModel: ReelViewModel by viewModels()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReelDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        shimmerFrameLayout = binding.shimmerViewContainer
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val reelId = intent.getStringExtra("reelId")
        setupViewPager()
        observeReels(reelId)
    }

    private fun observeReels(reelId: String?) {
        lifecycleScope.launch {
            reelViewModel.getReels().collectLatest { pagingData ->
                val filteredPagingData = pagingData.filter { it._id == reelId }
                reelAdapter.submitData(filteredPagingData)
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
            reelViewModel.likeReelResponse.observe(this) { response ->
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
            supportFragmentManager,
            BottomSheetCommentReelFragment::class.java.simpleName,
        )
    }

    companion object {
        private const val ARG_REEL_ID = "reel_id"

        fun newInstance(reelId: String): ReelDetailFragment {
            val fragment = ReelDetailFragment()
            val args = Bundle()
            args.putString(ARG_REEL_ID, reelId)
            fragment.arguments = args
            return fragment
        }
    }
}
