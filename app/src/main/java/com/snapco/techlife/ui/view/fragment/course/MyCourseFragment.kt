package com.snapco.techlife.ui.view.fragment.course

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.data.course.OtherCourseAdapter
import com.snapco.techlife.databinding.FragmentMyCourseBinding
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class MyCourseFragment : Fragment() {
    private lateinit var viewModel: CourseViewModel
    private lateinit var binding: FragmentMyCourseBinding
    private lateinit var courseAdapter: OtherCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate layout using View Binding
        binding = FragmentMyCourseBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CourseViewModel::class.java)

        // Thiết lập LayoutManager cho RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter rỗng ban đầu cho RecyclerView
        courseAdapter = OtherCourseAdapter(mutableListOf(), viewModel)
        binding.recyclerView.adapter = courseAdapter

        // Quan sát danh sách khóa học từ ViewModel
        viewModel.courses.observe(
            viewLifecycleOwner,
            Observer { courseList ->
                Log.d("MyCourseFragment", "Courses updated: $courseList")
                courseAdapter.updateCourses(courseList)
            },
        )

        // Quan sát thông báo lỗi từ ViewModel
        viewModel.errorMessage.observe(
            viewLifecycleOwner,
            Observer { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            },
        )

        //            val idUser = UserDataHolder.getUserId()
        val idUser = "672de65842792ca1976f11b9"

        viewModel.fetchCoursesByUser(idUser)

        // Trả về root view của binding để sử dụng với View Binding
        return binding.root
    }
}
