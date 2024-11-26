package com.snapco.techlife.ui.view.fragment.course

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityCourseBinding
import com.snapco.techlife.databinding.ActivityCourseFilterBinding
import com.snapco.techlife.databinding.ActivityMainBinding
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.MainActivity

class CourseFilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener{
            finish()
        }


    }

}