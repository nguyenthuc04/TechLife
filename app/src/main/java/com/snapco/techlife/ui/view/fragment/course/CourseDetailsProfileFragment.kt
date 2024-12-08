package com.snapco.techlife.ui.view.fragment.course

import RegisteredUsersAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.databinding.FragmentCourseDetailsProfileBinding
import com.snapco.techlife.extensions.formatPrice
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.messenger.ChatActivity
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.messenger.ChannelViewModel
import io.getstream.chat.android.client.ChatClient

class CourseDetailsProfileFragment : Fragment(),RegisteredUsersAdapter.ClickChat {
    private lateinit var binding: FragmentCourseDetailsProfileBinding
    private val courseActivityViewModel: CourseViewModel by activityViewModels()
    private lateinit var registeredUsersAdapter: RegisteredUsersAdapter
    private val client: ChatClient by lazy { ChatClient.instance() }
    private val listChannelViewModel: ChannelViewModel by viewModels()

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
            binding.courseDate.text = "Ngày bắt đầu: ${course.startDate}"
            binding.coursePrice.text = course.price.toInt().formatPrice()
            binding.courseDuration.text = course.duration
            binding.courseDescription.text = course.describe
            registeredUsersAdapter.updateUsers(course.user)
        }
    }

    private fun setupRecyclerView() {
        registeredUsersAdapter = RegisteredUsersAdapter(mutableListOf(),this)
        binding.registeredUsersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = registeredUsersAdapter
        }
    }

    override fun clickMess(id: String) {
        val idUserSearch = id // id nguoi dc tim kiem
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
