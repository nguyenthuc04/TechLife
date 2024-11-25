// CourseDetailsFragment.kt
package com.snapco.techlife.ui.view.fragment.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.snapco.techlife.databinding.FragmentCourseDetailsBinding
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class CourseDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCourseDetailsBinding
    private val courseActivityViewModel: CourseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Nút quay lại
        binding.toolbarCustomBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        courseActivityViewModel.coursesDetails.observe(viewLifecycleOwner) { course ->
            binding.toolbarCustomTitle.text = course.id
            binding.courseImage.loadImage(course.imageUrl)
            binding.courseName.text = course.name
            binding.courseDate.text = course.date
            binding.coursePrice.text = course.price
            binding.courseDuration.text = course.duration
            binding.courseDescription.text = course.describe
            binding.mentorImage.loadImage(course.userImageUrl)
            binding.mentorName.text = course.userName
        }
    }
}