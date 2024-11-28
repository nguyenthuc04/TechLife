// CourseDetailsFragment.kt
package com.snapco.techlife.ui.view.fragment.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.snapco.techlife.databinding.FragmentCourseDetailsBinding
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.data.model.RegisterCourseRequest
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            binding.courseImage.loadImage(course.imageUrl)
            binding.courseName.text = course.name
            binding.courseDate.text = course.date
            binding.coursePrice.text = course.price
            binding.courseDuration.text = course.duration
            binding.courseDescription.text = course.describe
            binding.mentorImage.loadImage(course.userImageUrl)
            binding.mentorName.text = course.userName

            // Kiểm tra nếu người dùng đã đăng ký khóa học
            val currentUserId = UserDataHolder.getUserId()
            if (currentUserId != null) {
                val isRegistered = isUserRegistered(course, currentUserId)
                binding.registerButton.visibility = if (isRegistered) View.GONE else View.VISIBLE
            } else {
                binding.registerButton.visibility = View.VISIBLE // Ẩn nếu không có user ID
            }
        }

        binding.registerButton.setOnClickListener {
            courseActivityViewModel.coursesDetails.value?.let {
                handleRegisterOrCancelCourse(it)
            }
        }
    }

    private fun handleRegisterOrCancelCourse(course: Course) {
        val currentUserId = UserDataHolder.getUserId()
        val currentUserName = UserDataHolder.getUserName()
        val currentUserAvatar = UserDataHolder.getUserAvatar()

        if (currentUserId == null || currentUserName == null) {
            Toast.makeText(requireContext(), "Thông tin người dùng không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        // Luôn thực hiện đăng ký (bỏ kiểm tra đã đăng ký trước đó)
        showConfirmationDialog(
            title = "Xác nhận đăng ký",
            message = "Bạn có muốn đăng ký khóa học '${course.name}' không?",
            onConfirm = {
                registerCourse(course, currentUserId, currentUserName, currentUserAvatar)
            }
        )
    }

    private fun registerCourse(course: Course, userId: String, userName: String, avatar: String?) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        // Tạo request để gửi lên server
        val registerRequest = RegisterCourseRequest(
            id = userId,
            userName = userName,
            avatar = avatar ?: "",
            date = currentDate,
        )

        courseActivityViewModel.registerCourse(course.id ?: "", registerRequest)

        // Quan sát kết quả đăng ký
        courseActivityViewModel.registerCourseResponse.observe(viewLifecycleOwner) { response ->
            if (response.success) {
                Toast.makeText(requireContext(), "Đăng ký khóa học thành công!", Toast.LENGTH_SHORT).show()
                binding.registerButton.visibility = View.GONE // Ẩn nút ngay sau khi đăng ký
                courseActivityViewModel.getCoursesByUser(userId) // Cập nhật dữ liệu
            } else {
                Toast.makeText(requireContext(), "Đăng ký thất bại: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

// Bỏ hàm cancelRegistration vì không cần nữa
    private fun showConfirmationDialog(title: String, message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Có") { _, _ -> onConfirm() }
            .setNegativeButton("Không") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun isUserRegistered(course: Course, userId: String): Boolean {
        return course.user.any { it.userId == userId }
    }
}