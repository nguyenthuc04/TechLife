package com.snapco.techlife.ui.view.fragment.course

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.data.model.UpdateCourseRequest
import com.snapco.techlife.databinding.FragmentMyCourseBinding
import com.snapco.techlife.extensions.replaceFragment
import com.snapco.techlife.ui.view.activity.course.EditCourse
import com.snapco.techlife.ui.view.adapter.MyCourseAdapter
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class MyCourseFragment :
    Fragment(),
    MyCourseAdapter.OnCourseActionListener {
    private val courseViewModel: CourseViewModel by viewModels()
    private lateinit var binding: FragmentMyCourseBinding
    private lateinit var courseAdapter: MyCourseAdapter
    private var currentPopupMenu: PopupMenu? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMyCourseBinding.inflate(inflater, container, false)

        binding.imageView13.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        courseAdapter = MyCourseAdapter(mutableListOf(), this){
            replaceFragment(CourseDetailsProfileFragment())
        }

        binding.recyclerView.adapter = courseAdapter
        UserDataHolder.getUserId()?.let { courseViewModel.getCoursesByUser(it) }

        // Observe course list from ViewModel
        courseViewModel.courseUserResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.courses.isNullOrEmpty()) {
                    Log.e("MyCourseFragment", "Course list is null or empty")
                    return@observe
                }
                courseAdapter.updateCourses(it.courses.reversed())
            } ?: run {
                Log.e("MyCourseFragment", "Response is null")
            }
        }

        // Observe delete course response from ViewModel
        courseViewModel.deleteCourseResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.success) {
                    UserDataHolder.getUserId()?.let { userId -> courseViewModel.getCoursesByUser(userId) }
                } else {
                    Toast.makeText(requireContext(), "Xóa khóa học thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT_COURSE && resultCode == Activity.RESULT_OK) {
            // Reload data
            UserDataHolder.getUserId()?.let { courseViewModel.getCoursesByUser(it) }
        }
    }

    override fun showPopupMenu(
        it: View?,
        course: Course,
        position: Int,
    ) {
        val popup = PopupMenu(it?.context, it)
        popup.menuInflater.inflate(R.menu.menu_course, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_update -> {
                    val updateCourseRequest =
                        UpdateCourseRequest(
                            name = course.name,
                            quantity = course.quantity,
                            imageUrl = course.imageUrl,
                            price = course.price,
                            duration = course.duration,
                            describe = course.describe,
                        )
                    val intent = Intent(requireContext(), EditCourse::class.java)
                    intent.putExtra("updateCourseRequest", updateCourseRequest)
                    intent.putExtra("courseId", course.id)
                    startActivityForResult(intent, REQUEST_CODE_EDIT_COURSE)
                }

                R.id.action_delete -> delete(course)
            }
            true
        }
        popup.show()
    }

    private fun showDeleteConfirmationDialog(course: Course) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Xác nhận xóa")
        builder.setMessage("Bạn có chắc chắn muốn xóa khóa học này?")
        builder.setPositiveButton("Đồng ý") { dialog, _ ->
            deleteCourse(course)
            dialog.dismiss()
        }
        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun deleteCourse(course: Course) {
        course.id?.let { courseViewModel.deleteCourse(it) }
    }

    private fun delete(course: Course) {
        showDeleteConfirmationDialog(course)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentPopupMenu?.dismiss()
    }

    companion object {
        private const val REQUEST_CODE_EDIT_COURSE = 1
    }
}
