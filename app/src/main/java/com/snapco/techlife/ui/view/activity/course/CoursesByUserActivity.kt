package com.snapco.techlife.ui.view.activity.course

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityCoursesByUserBinding
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.messenger.ChatActivity
import com.snapco.techlife.ui.view.adapter.CoursesByUserAdapter
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.messenger.ChannelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import io.getstream.chat.android.client.ChatClient

class CoursesByUserActivity :
    AppCompatActivity(),
    CoursesByUserAdapter.OnCoursebyUserActionListener {
    private lateinit var binding: ActivityCoursesByUserBinding
    private val courseViewModel: CourseViewModel by viewModels()
    private lateinit var coursesByUserAdapter: CoursesByUserAdapter
    private val client: ChatClient by lazy { ChatClient.instance() }
    private val listChannelViewModel: ChannelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCoursesByUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        UserDataHolder.getUserId()?.let { courseViewModel.getCoursesByUserRegistration(it) }
        observeCourses()
    }

    private fun observeCourses() {
        courseViewModel.coursesByUserResponse.observe(this) { coursesByUser ->
            coursesByUser?.let {
                coursesByUserAdapter.updateCourses(coursesByUser.courses)
            }
        }
    }

    private fun setupRecyclerView() {
        coursesByUserAdapter = CoursesByUserAdapter(mutableListOf(), this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coursesByUserAdapter
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar5)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun clickMes(userSlect: String) {
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
                        listChannelViewModel.createChannel(idCheck2, idUserSearch, extraData)
                    }
                }
            }
        }
    }
}
