package com.snapco.techlife.ui.view.fragment.course

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.databinding.FragmentOtherCoursesBinding
import com.snapco.techlife.ui.view.adapter.OtherCourseAdapter
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class OtherCoursesFragment : Fragment() {
    private val courseViewModel: CourseViewModel by viewModels()
    private lateinit var binding: FragmentOtherCoursesBinding
    private lateinit var othercourseAdapter: OtherCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOtherCoursesBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        othercourseAdapter = OtherCourseAdapter(mutableListOf())
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
