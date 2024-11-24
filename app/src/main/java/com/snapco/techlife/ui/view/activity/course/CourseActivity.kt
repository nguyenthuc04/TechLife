package com.snapco.techlife.ui.view.activity.course

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityCourseBinding
import com.snapco.techlife.ui.view.fragment.course.PagerAdapter
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class CourseActivity : AppCompatActivity() {
    private lateinit var viewModel: CourseViewModel
    private lateinit var binding: ActivityCourseBinding
    private val tabTitles = arrayOf("My Course", "Other Courses")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Sử dụng View Binding để inflate layout
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Điều chỉnh padding cho hệ thống thanh trạng thái và điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Thiết lập ViewPager và TabLayout
        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // Xử lý sự kiện nút "Add"
        binding.toolbar.findViewById<ImageButton>(R.id.btn_add).setOnClickListener {
            val intent = Intent(this, AddCourse::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_COURSE)
        }

        // Xử lý sự kiện nút "Back"
        binding.toolbar.findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            onBackPressed()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_COURSE && resultCode == AppCompatActivity.RESULT_OK) {
//            viewModel.fetchCourses() // Lấy lại danh sách khóa học sau khi thêm
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_COURSE = 1
    }
}
