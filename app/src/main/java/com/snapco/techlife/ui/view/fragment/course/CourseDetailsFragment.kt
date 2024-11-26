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
import com.snapco.techlife.data.model.UpdateCourseRequest
import com.snapco.techlife.data.model.UserCourse
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

    private fun handleRegisterOrCancelCourse(course: Course) {
        val currentUserId = UserDataHolder.getUserId()
        val currentUserName = UserDataHolder.getUserName()

        if (currentUserId == null || currentUserName == null) {
            Toast.makeText(requireContext(), "Thông tin người dùng không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        // Kiểm tra xem người dùng đã đăng ký khóa học này chưa
        val existingUserCourse = course.user.find { it.userId == currentUserId }

        if (existingUserCourse != null) {
            // Nếu người dùng đã đăng ký, hiển thị Dialog hỏi muốn hủy đăng ký không
            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận hủy đăng ký")
                .setMessage("Bạn có muốn hủy đăng ký khóa học '${course.name}' này không?")
                .setPositiveButton("Có") { _, _ ->
                    // Xử lý khi người dùng chọn "Có" để hủy đăng ký
                    cancelRegistration(course)
                }
                .setNegativeButton("Không") { dialog, _ ->
                    // Đóng dialog khi người dùng chọn "Không"
                    dialog.dismiss()
                }
                .create()

            // Hiển thị Dialog
            alertDialog.show()
        } else {
            // Nếu người dùng chưa đăng ký, tiến hành đăng ký khóa học
            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đăng ký")
                .setMessage("Bạn có muốn đăng ký khóa học '${course.name}' này không?")
                .setPositiveButton("Có") { _, _ ->
                    // Xử lý khi người dùng chọn "Có" để đăng ký
                    registerCourse(course)
                }
                .setNegativeButton("Không") { dialog, _ ->
                    // Đóng dialog khi người dùng chọn "Không"
                    dialog.dismiss()
                }
                .create()

            // Hiển thị Dialog
            alertDialog.show()
        }
    }

    private fun registerCourse(course: Course) {
        // Lấy thông tin người dùng (ID, tên, avatar)
        val currentUserId = UserDataHolder.getUserId() // Cần thay bằng ID người dùng thật
        val currentUserName = UserDataHolder.getUserName() // Thay bằng tên người dùng thật
        val currentUserImageUrl = UserDataHolder.getUserAvatar() // Thay bằng URL ảnh đại diện của người dùng

        // Kiểm tra nếu thông tin người dùng là null, xử lý lỗi
        if (currentUserId == null || currentUserName == null) {
            // Nếu ID hoặc tên người dùng là null, hiển thị thông báo lỗi và thoát khỏi hàm
            Toast.makeText(requireContext(), "Thông tin người dùng không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        // Lấy thời gian hiện tại dưới định dạng "yyyy-MM-dd HH:mm:ss"
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        // Cập nhật mảng user của khóa học
        // Tạo đối tượng UserCourse cho người dùng đăng ký mới
        val newUserCourse = UserCourse(
            userId = currentUserId, // ID người dùng đã xác thực
            name = currentUserName, // Tên người dùng đã xác thực
            date = currentDate, // Ngày hiện tại
            status = "Đã đăng ký" // Trạng thái đăng ký
        )
        // Cập nhật mảng user của khóa học, thêm người dùng mới vào danh sách
        val updatedUserList = course.user.toMutableList() // Copy danh sách hiện tại
        updatedUserList.add(newUserCourse) // Thêm người dùng mới vào danh sách

        // Tạo đối tượng UpdateCourseRequest với thông tin cần cập nhật
        val updateCourseRequest = UpdateCourseRequest(
            name = course.name, // Giữ nguyên tên khóa học
            quantity = (course.quantity.toInt() - 1).toString(), // Giảm số lượng khi đăng ký
            imageUrl = course.imageUrl, // Giữ nguyên hình ảnh
            price = course.price, // Giữ nguyên giá
            duration = course.duration, // Giữ nguyên thời gian
            describe = course.describe, // Giữ nguyên mô tả
        )

        // Gọi hàm updateCourse để cập nhật khóa học với danh sách người dùng mới
        courseActivityViewModel.updateCourse(course.id ?: "", updateCourseRequest)

        // Quan sát kết quả từ viewModel để cập nhật UI hoặc thông báo cho người dùng
        courseActivityViewModel.updateCourseResponse.observe(viewLifecycleOwner) { response ->
            if (response.success) {
                // Cập nhật danh sách người dùng thành công, có thể thêm logic khác nếu cần
            } else {
                // Thông báo lỗi khi cập nhật
            }
        }
    }

    private fun cancelRegistration(course: Course) {
        val currentUserId = UserDataHolder.getUserId()

        // Lọc người dùng khỏi danh sách user
        val updatedUserList = course.user.filterNot { it.userId == currentUserId }

        // Tạo đối tượng UpdateCourseRequest chỉ chứa danh sách user
        val updateCourseRequest = UpdateCourseRequest(
            name = course.name,
            quantity = (course.quantity.toInt() - 1).toString(), // Giảm số lượng khi hủy đăng ký
            imageUrl = course.imageUrl,
            price = course.price,
            duration = course.duration,
            describe = course.describe
        )

        // Gọi hàm updateCourse để cập nhật khóa học với danh sách người dùng đã sửa đổi
        courseActivityViewModel.updateCourse(course.id ?: "", updateCourseRequest)

        // Quan sát kết quả từ viewModel để cập nhật UI hoặc thông báo cho người dùng
        courseActivityViewModel.updateCourseResponse.observe(viewLifecycleOwner) { response ->
            if (response.success) {
                Toast.makeText(requireContext(), "Hủy đăng ký thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Hủy đăng ký thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }
}