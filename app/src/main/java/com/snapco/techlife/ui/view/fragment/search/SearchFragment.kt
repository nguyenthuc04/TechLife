package com.snapco.techlife.ui.view.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.databinding.FragmentSearchBinding
import com.snapco.techlife.databinding.PostitemBinding
import com.snapco.techlife.extensions.replaceFragment
import com.snapco.techlife.ui.view.adapter.ImageAdapter
import com.snapco.techlife.ui.view.adapter.SearchAdapter
import com.snapco.techlife.ui.view.fragment.home.HomeFragment
import com.snapco.techlife.ui.viewmodel.SearchViewModel
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchActivityViewModel: SearchViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            FragmentSearchBinding.inflate(inflater, container, false).apply {
                viewModel = searchViewModel
                lifecycleOwner = viewLifecycleOwner
            }
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter(emptyList()) { user ->
            searchActivityViewModel.setUser(user)
            replaceFragment(SearchProfileFragment())
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = searchAdapter

        searchViewModel.users.observe(
            viewLifecycleOwner,
            Observer { users ->
                val filteredUsers = users.filter { user ->
                    user.accountType != "admin" && user.accountType != "staff"
                }
                searchAdapter.updateData(filteredUsers)
            },
        )

        searchViewModel.isSearching.observe(
            viewLifecycleOwner,
            Observer { isSearching ->
                binding.recyclerView.visibility = if (isSearching) View.VISIBLE else View.GONE
            },
        )

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchViewModel.searchUsers(newText ?: "")
                    return true
                }
            },
        )

        binding.recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int,
                ) {
                    if (dy > 0) {
                        binding.searchView.visibility = View.GONE
                    } else if (dy < 0) {
                        binding.searchView.visibility = View.VISIBLE
                    }
                }
            },
        )

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cancelText.visibility = View.VISIBLE
                binding.recyclerPostView.visibility = View.GONE // Ẩn recyclerPostView khi tìm kiếm
            } else {
                binding.cancelText.visibility = View.GONE
            }
        }

        binding.cancelText.setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()
            binding.cancelText.visibility = View.GONE
            binding.recyclerPostView.visibility = View.VISIBLE // Hiện recyclerPostView khi bấm hủy
        }

        val randomPostAdapter = ImagePostAdapter(emptyList())
        binding.recyclerPostView.layoutManager =
            GridLayoutManager(requireContext(), 3) // Sử dụng GridLayoutManager với 3 cột
        binding.recyclerPostView.adapter = randomPostAdapter

        // Lấy danh sách bài viết từ API
        homeViewModel.getListPosts()

        // Lắng nghe dữ liệu bài viết từ ViewModel
        homeViewModel.posts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                val randomPosts = posts.shuffled() // Xáo trộn danh sách bài viết
                randomPostAdapter.updateData(randomPosts) // Cập nhật Adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (searchViewModel.isSearching.value == true) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.recyclerPostView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.recyclerPostView.visibility = View.VISIBLE
        }
    }
}

class ImagePostAdapter(private var posts: List<Post>) :
    RecyclerView.Adapter<ImagePostAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PostitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {

            val firstImageUrl = post.imageUrl?.get(0)

            Glide.with(binding.root.context)
                .load(firstImageUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(binding.postImage)
            binding.postImage.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("postId", post._id)
                }

                val postDetailfrg = PostDetailFragment().apply {
                    arguments = bundle
                }
                itemView.context
                    .let { context ->
                        (context as? FragmentActivity)?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.frameLayout, postDetailfrg)  // Đảm bảo ID container đúng
                            ?.addToBackStack(null)
                            ?.commit()
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    fun updateData(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}



