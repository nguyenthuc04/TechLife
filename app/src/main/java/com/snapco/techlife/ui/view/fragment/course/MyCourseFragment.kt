package com.snapco.techlife.ui.view.fragment.course

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.databinding.FragmentMyCourseBinding
import com.snapco.techlife.data.model.course.MyCourseAdapter
import com.snapco.techlife.ui.view.activity.course.AddCourse
import com.snapco.techlife.ui.viewmodel.CourseViewModel


class MyCourseFragment : Fragment() {
    private lateinit var viewModel: CourseViewModel
    private lateinit var binding: FragmentMyCourseBinding
    private lateinit var courseAdapter: MyCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout using View Binding
        binding = FragmentMyCourseBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CourseViewModel::class.java)

        // Thiết lập LayoutManager cho RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter rỗng ban đầu cho RecyclerView
        courseAdapter = MyCourseAdapter(mutableListOf(), viewModel)
        binding.recyclerView.adapter = courseAdapter

        // Quan sát danh sách khóa học từ ViewModel
        viewModel.courses.observe(viewLifecycleOwner, Observer { courseList ->
            Log.d("MyCourseFragment", "Courses updated: $courseList")
            courseAdapter.updateCourses(courseList)
        })

        // Quan sát thông báo lỗi từ ViewModel
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        //            val idUser = UserDataHolder.getUserId()
        val idUser = "672de65842792ca1976f11b9"

        viewModel.fetchCoursesByUser(idUser)

        // Xử lý sự kiện khi bấm vào nút Thêm Khóa Học
        binding.btnAddCourse.setOnClickListener {
            val intent = Intent(activity, AddCourse::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_COURSE)
        }

        // Trả về root view của binding để sử dụng với View Binding
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_COURSE && resultCode == AppCompatActivity.RESULT_OK) {
            viewModel.fetchCourses() // Lấy lại danh sách khóa học sau khi thêm
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_COURSE = 1
    }
}
