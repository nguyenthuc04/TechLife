// CourseDetailsFragment.kt
package com.snapco.techlife.ui.view.fragment.course

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.data.model.RegisterCourseRequest
import com.snapco.techlife.databinding.FragmentCourseDetailsBinding
import com.snapco.techlife.extensions.formatPrice
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.activity.messenger.ChatActivity
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.messenger.ChannelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import io.getstream.chat.android.client.ChatClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CourseDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCourseDetailsBinding
    private val courseActivityViewModel: CourseViewModel by activityViewModels()
    private val client: ChatClient by lazy { ChatClient.instance() }
    private val listChannelViewModel: ChannelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // Nút quay lại
        binding.toolbarCustomBack.setOnClickListener {
            requireActivity().onBackPressed()
        }



        courseActivityViewModel.coursesDetails.observe(viewLifecycleOwner) { course ->
            binding.courseImage.loadImage(course.imageUrl)
            binding.courseName.text = course.name
            binding.courseDateStart.text = "Ngày bắt đầu: ${course.startDate}"
            binding.courseDateEnd.text = "đến: ${course.endDate}"
            binding.coursePrice.text = course.price.toInt().formatPrice()
            binding.courseDuration.text = "Thời lượng: ${course.duration}"
            binding.courseDescription.text = course.describe
            binding.mentorImage.loadImage(course.userImageUrl)
            binding.mentorName.text = course.userName
            binding.mentorPhoneNumber.text = "Liên hệ: ${course.phoneNumber}"
            Log.d("CourseDetailsFragment", "Course: $course")

            course.userId?.let { clickMes(it) }
            // Kiểm tra nếu người dùng đã đăng ký khóa học
            val currentUserId = UserDataHolder.getUserId()


            if (currentUserId != null) {
                val isRegistered = isUserRegistered(course, currentUserId)
                if(currentUserId == course.userId){
                    binding.registerButton.gone()
                } else {
                    Log.d("CourseDetailsFragment", "isRegistered: $isRegistered")
                    binding.registerButton.visibility = if (isRegistered) View.GONE else View.VISIBLE
                    binding.NextMess.visibility = if (isRegistered) View.VISIBLE else View.GONE
                }

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
            },
        )
    }

    private fun registerCourse(
        course: Course,
        userId: String,
        userName: String,
        avatar: String?,
    ) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        // Tạo request để gửi lên server
        val registerRequest =
            RegisterCourseRequest(
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
    private fun showConfirmationDialog(
        title: String,
        message: String,
        onConfirm: () -> Unit,
    ) {
        AlertDialog
            .Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Có") { _, _ -> onConfirm() }
            .setNegativeButton("Không") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun isUserRegistered(
        course: Course,
        userId: String,
    ): Boolean = course.user.any { it.id == userId }

    override fun onResume() {
        courseActivityViewModel.getCoursesByUser(UserDataHolder.getUserId().toString())
        super.onResume()
    }

    private fun clickMes (userSlect : String) {
        binding.NextMess.setOnClickListener {
            val idUserSearch = userSlect // id nguoi dc tim kiem
            val idMe = (client.getCurrentUser()?.id ?: "") // id nguoi dung
            val idCheck1 = idUserSearch + idMe
            val idCheck2 = idMe + idUserSearch

            listChannelViewModel.checkChannelExists(idCheck1) { check1 ->
                if (check1) {
                    // Kênh đã tồn tại với idChannel
                    startActivity<ChatActivity> {
                        putExtra("ID", idCheck1)
                    }
                } else {
                    // Nếu idChannel không tồn tại, kiểm tra tiếp idCheck
                    listChannelViewModel.checkChannelExists(idCheck2) { check2 ->
                        if (check2) {
                            // Kênh đã tồn tại với idCheck
                            startActivity<ChatActivity> {
                                putExtra("ID", idCheck2)
                            }
                        } else {
                            val extraData =
                                mapOf(
                                    "user_create" to client.getCurrentUser()!!.name, // Tên người dùng hiện tại
                                )
                            // Nếu cả idChannel và idCheck không tồn tại, tạo kênh mới với idChannel
                            listChannelViewModel.createChannel(idCheck2, idUserSearch,extraData)
                        }
                    }
                }
            }
        }
    }

}
