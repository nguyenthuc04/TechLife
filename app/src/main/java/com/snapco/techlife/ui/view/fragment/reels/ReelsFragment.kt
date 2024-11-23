package com.snapco.techlife.ui.view.fragment.reels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Reel
import com.snapco.techlife.databinding.FragmentReelsBinding
import com.snapco.techlife.ui.view.adapter.ReelAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetCommentReelFragment
import com.snapco.techlife.ui.viewmodel.ReelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class ReelsFragment :
    Fragment(),
    ReelAdapter.OnReelActionListener {
    private lateinit var binding: FragmentReelsBinding
    private lateinit var reelAdapter: ReelAdapter
    private val reelViewModel: ReelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reels, container, false)
        setupviewPager()
        reelViewModel.getListReel()
        observePosts()

        return binding.root
    }

    private fun observePosts() {
        reelViewModel.reel.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                reelAdapter.updateReel(it.reversed())
            }
        }
    }

    private fun setupviewPager() {
        reelAdapter = ReelAdapter(mutableListOf(), this)
        binding.viewPager.adapter = reelAdapter
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
        post: Reel,
        position: Int,
    ) {
        reelAdapter.updateLikeButtonAt(position)

        UserDataHolder.getUserId()?.let {
            reelViewModel.likeReel(post._id, it)
        }
    }

    override fun onCommentPost(
        postId: String,
        position: Int,
    ) {
        val bottomSheet = BottomSheetCommentReelFragment.newInstance(postId)
        bottomSheet.show(parentFragmentManager, BottomSheetCommentReelFragment::class.java.simpleName)
        reelAdapter.updateCommentCountAt(position)
    }
}
