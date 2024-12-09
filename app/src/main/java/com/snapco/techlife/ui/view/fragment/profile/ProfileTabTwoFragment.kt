package com.snapco.techlife.ui.view.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.databinding.FragmentProfileTabTwoBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.adapter.ReelProfileAdapter
import com.snapco.techlife.ui.view.fragment.reels.ReelDetailFragment
import com.snapco.techlife.ui.view.fragment.reels.ReelsFragment
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import kotlinx.coroutines.launch

class ProfileTabTwoFragment : Fragment() {
    private lateinit var binding: FragmentProfileTabTwoBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var reelAdapter: ReelProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("ProfileTabTwoFragment", "onCreateView called")
        binding = FragmentProfileTabTwoBinding.inflate(inflater, container, false)
        setupRecyclerView()
        arguments?.getString("user_id")?.let {
            Log.d("ProfileTabTwoFragment", "User ID: $it")
            homeViewModel.getReelsByUser(it)
        }
        observeReels()
        return binding.root
    }

    private fun setupRecyclerView() {
        reelAdapter = ReelProfileAdapter(mutableListOf()) { reel ->
            openReelsFragment(reel._id)
        }
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = reelAdapter
        }
    }
    private fun openReelsFragment(reelId: String) {
        val fragment = ReelDetailFragment.newInstance(reelId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun observeReels() {
        homeViewModel.reelListProfile.observe(viewLifecycleOwner) { reelList ->
            Log.d("ProfileTabTwoFragment", "Reel list observed: $reelList")
            if (reelList == null || reelList.reels.isNullOrEmpty()) {
                binding.noReelsMessage.visibility = View.VISIBLE
                binding.imageView10.visible()
            } else {
                binding.noReelsMessage.visibility = View.GONE
                binding.imageView10.gone()
                reelAdapter.updateReels(reelList.reels.reversed())
            }
        }
    }


    companion object {
        fun newInstance(userId: String): ProfileTabTwoFragment {
            val fragment = ProfileTabTwoFragment()
            val args = Bundle()
            args.putString("user_id", userId)
            fragment.arguments = args
            return fragment
        }
    }
}