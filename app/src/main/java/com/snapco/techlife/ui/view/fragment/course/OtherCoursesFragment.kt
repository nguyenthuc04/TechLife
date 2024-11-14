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
import com.snapco.techlife.R
import com.snapco.techlife.data.model.course.MyCourseAdapter
import com.snapco.techlife.data.model.course.OtherCourseAdapter
import com.snapco.techlife.databinding.FragmentMyCourseBinding
import com.snapco.techlife.databinding.FragmentOtherCoursesBinding
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class OtherCoursesFragment : Fragment() {
    private lateinit var viewModel: CourseViewModel
    private lateinit var binding: FragmentOtherCoursesBinding
    private lateinit var othercourseAdapter: OtherCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtherCoursesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CourseViewModel::class.java)

        binding.recyclerViewOther.layoutManager = LinearLayoutManager(requireContext())

        othercourseAdapter = OtherCourseAdapter(mutableListOf(), viewModel)
        binding.recyclerViewOther.adapter = othercourseAdapter

        viewModel.courses.observe(viewLifecycleOwner, Observer { courseList ->
            Log.d("MyCourseFragment", "Courses updated: $courseList")
            othercourseAdapter.updateCourses(courseList)
        })

        // Quan sát thông báo lỗi từ ViewModel
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })



        viewModel.fetchCourses()

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