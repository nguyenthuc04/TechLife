package com.snapco.techlife.ui.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.snapco.techlife.databinding.FragmentProfileTabTwoBinding
import com.snapco.techlife.ui.view.adapter.ReelProfileAdapter
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel

class ProfileTabTwoFragment : Fragment() {
    private lateinit var binding: FragmentProfileTabTwoBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var reelAdapter: ReelProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileTabTwoBinding.inflate(inflater, container, false)
        setupRecyclerView()
//        UserDataHolder.getUserId()?.let { homeViewModel.getReelsByUser(it) }
        arguments?.getString("user_id")?.let { homeViewModel.getReelsByUser(it) }
        observeReels()
        return binding.root
    }

    private fun setupRecyclerView() {
        reelAdapter = ReelProfileAdapter(mutableListOf())
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = reelAdapter
        }
    }

    private fun observeReels() {
        homeViewModel.reelListProfile.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                reelAdapter.updateReels(it.reels!!.reversed())
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
