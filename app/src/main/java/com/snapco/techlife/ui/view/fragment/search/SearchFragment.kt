package com.snapco.techlife.ui.view.fragment.search

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
import com.snapco.techlife.R
import com.snapco.techlife.databinding.FragmentSearchBinding
import com.snapco.techlife.ui.adapter.SearchAdapter
import com.snapco.techlife.ui.viewmodel.SearchViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }

        searchViewModel.users.observe(viewLifecycleOwner, Observer { users ->
            searchAdapter.updateData(users)
        })

        searchViewModel.isSearching.observe(viewLifecycleOwner, Observer { isSearching ->
            binding.recyclerView.visibility = if (isSearching) View.VISIBLE else View.GONE
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel.searchUsers(newText ?: "")
                return true
            }
        })

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
    }
}