package com.snapco.techlife.ui.view.fragment.course

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.data.model.course.CourseAdapter
import com.snapco.techlife.ui.view.activity.course.AddCourse
import com.snapco.techlife.ui.viewmodel.CourseViewModel


class MyCourseFragment : Fragment() {
    private lateinit var viewModel: CourseViewModel
    private lateinit var btnAddCourse: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_course, container, false)
        viewModel = ViewModelProvider(this).get(CourseViewModel::class.java)

        btnAddCourse = view.findViewById(R.id.btnAddCourse)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Thiết lập LayoutManager cho RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter rỗng ban đầu cho RecyclerView
        courseAdapter = CourseAdapter(mutableListOf(), viewModel)
        recyclerView.adapter = courseAdapter

        viewModel.courses.observe(viewLifecycleOwner, Observer { courseList ->
            Log.d("MyCourseFragment", "Courses updated: $courseList")
            courseAdapter.updateCourses(courseList)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })



        // Gọi phương thức lấy danh sách khóa học
        viewModel.fetchCourses()

        btnAddCourse.setOnClickListener {
            val intent = Intent(activity, AddCourse::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_COURSE)
        }

        return view
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
