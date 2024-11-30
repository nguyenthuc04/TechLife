package com.snapco.techlife.ui.view.fragment.course

import RegisteredUsersAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.databinding.FragmentCourseDetailsProfileBinding
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class CourseDetailsProfileFragment : Fragment() {
    private lateinit var binding: FragmentCourseDetailsProfileBinding
    private val courseActivityViewModel: CourseViewModel by activityViewModels()
    private lateinit var registeredUsersAdapter: RegisteredUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCourseDetailsProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // Back button
        binding.toolbarCustomBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        // Setup RecyclerView for registered users
        setupRecyclerView()

        // Observe course details
        courseActivityViewModel.coursesDetails.observe(viewLifecycleOwner) { course ->
            binding.courseImage.loadImage(course.imageUrl)
            binding.courseName.text = course.name
            binding.courseDate.text = course.date
            binding.coursePrice.text = course.price
            binding.courseDuration.text = course.duration
            binding.courseDescription.text = course.describe
            registeredUsersAdapter.updateUsers(course.user)
        }
    }

    private fun setupRecyclerView() {
        registeredUsersAdapter = RegisteredUsersAdapter(mutableListOf())
        binding.registeredUsersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = registeredUsersAdapter
        }
    }
}
