package com.snapco.techlife.ui.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.snapco.techlife.R
import com.snapco.techlife.ui.view.fragment.course.PagerAdapter

class CourseActivity : AppCompatActivity() {
    private lateinit var view_pager: ViewPager2
    private lateinit var tab_layout: TabLayout
    private val tabTitles = arrayOf("My Course", "Other Courses")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_course)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tab_layout = findViewById(R.id.tab_layout)
        view_pager = findViewById(R.id.view_pager)



        val pagerAdapter = PagerAdapter(this)
        view_pager.adapter = pagerAdapter

        // Thiết lập TabLayout với ViewPager
        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}