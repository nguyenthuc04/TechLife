package com.snapco.techlife.ui.view.fragment.course

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.data.model.UpdateCourseRequest
import com.snapco.techlife.data.model.UserCourse
import com.snapco.techlife.databinding.FragmentOtherCoursesBinding
import com.snapco.techlife.extensions.replaceFragment
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.adapter.OtherCourseAdapter
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.SearchViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import io.getstream.chat.android.models.Filters

class OtherCoursesFragment : Fragment() {
    private val courseViewModel: CourseViewModel by viewModels()
    private val courseActivityViewModel: CourseViewModel by activityViewModels()

    private lateinit var binding: FragmentOtherCoursesBinding
    private lateinit var otherCourseAdapter: OtherCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOtherCoursesBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter với callback xử lý nút
        otherCourseAdapter = OtherCourseAdapter(
            mutableListOf(),
            onItemClickListener = { course ->
                // Xử lý khi bấm vào item
                courseActivityViewModel.setCours(course)
                replaceFragment(CourseDetailsFragment())
            },
        )

        binding.imgFilter.setOnClickListener {
            startActivity<CourseFilterActivity>()
        }

        binding.recyclerView.adapter = otherCourseAdapter

        courseViewModel.getListCourses()

        courseViewModel.courses.observe(viewLifecycleOwner) { courseList ->
            otherCourseAdapter.updateCourses(courseList)
        }

        binding.edtSearchCourse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                courseViewModel.searchCourses(query)
                courseViewModel.searchCourses.observe(viewLifecycleOwner) { courseList ->
                    otherCourseAdapter.updateCourses(courseList)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return binding.root
    }

    @Deprecated("Deprecated in Java")
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
