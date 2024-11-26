package com.snapco.techlife.ui.view.fragment.course

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.data.model.UpdateCourseRequest
import com.snapco.techlife.data.model.UserCourse
import com.snapco.techlife.databinding.FragmentOtherCoursesBinding
import com.snapco.techlife.extensions.replaceFragment
import com.snapco.techlife.ui.view.adapter.OtherCourseAdapter
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.SearchViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OtherCoursesFragment : Fragment() {
    private val courseViewModel: CourseViewModel by viewModels()
    private val courseActivityViewModel: CourseViewModel by activityViewModels()

    private lateinit var binding: FragmentOtherCoursesBinding
    private lateinit var othercourseAdapter: OtherCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOtherCoursesBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter với callback xử lý nút
        othercourseAdapter = OtherCourseAdapter(
            mutableListOf(),
            onItemClickListener = { course ->
                // Xử lý khi bấm vào item
                courseActivityViewModel.setCours(course)
                replaceFragment(CourseDetailsFragment())
            },
        )

        binding.recyclerView.adapter = othercourseAdapter
        courseViewModel.getListCourses()
        courseViewModel.courses.observe(viewLifecycleOwner) { courseList ->
            othercourseAdapter.updateCourses(courseList)
        }
        return binding.root
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_COURSE && resultCode == AppCompatActivity.RESULT_OK) {
            courseViewModel.getListCourses()
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_COURSE = 1
    }
}
