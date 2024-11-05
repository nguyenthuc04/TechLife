package com.snapco.techlife.ui.view.fragment.search

import SearchAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.databinding.FragmentSearchBinding
import com.snapco.techlife.ui.view.activity.PremiumActivity
import com.snapco.techlife.ui.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    // Khởi tạo ViewModel cho chức năng tìm kiếm
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Khởi tạo layout bằng ViewBinding
        binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            viewModel = searchViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo adapter với danh sách rỗng
        searchAdapter = SearchAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = searchAdapter

        // Quan sát LiveData users từ ViewModel
        searchViewModel.users.observe(viewLifecycleOwner, Observer { users ->
            searchAdapter.updateData(users)
        })

        // Quan sát LiveData isSearching để hiển thị/ẩn RecyclerView
        searchViewModel.isSearching.observe(viewLifecycleOwner, Observer { isSearching ->
            binding.recyclerView.visibility = if (isSearching) View.VISIBLE else View.GONE
        })

        // Thiết lập listener cho SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Cập nhật truy vấn tìm kiếm trong ViewModel
                searchViewModel.searchUsers(newText ?: "")
                return true
            }
        })

        // Thêm listener cuộn cho RecyclerView để ẩn/hiện SearchView
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    // Cuộn xuống, ẩn SearchView
                    binding.searchView.visibility = View.GONE
                } else if (dy < 0) {
                    // Cuộn lên, hiện SearchView
                    binding.searchView.visibility = View.VISIBLE
                }
            }
        })

        // Hiển thị TextView Huỷ khi SearchView được focus
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cancelText.visibility = View.VISIBLE
            } else {
                binding.cancelText.visibility = View.GONE
            }
        }

        // Xử lý sự kiện khi bấm TextView Huỷ
        binding.cancelText.setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()
            binding.cancelText.visibility = View.GONE
        }

        // Xử lý sự kiện khi bấm nút Premium
        binding.btnPremium.setOnClickListener {
            val intent = Intent(requireContext(), PremiumActivity::class.java)
            startActivity(intent)
        }
    }
}