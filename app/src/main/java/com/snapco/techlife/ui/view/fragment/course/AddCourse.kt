package com.snapco.techlife.ui.view.fragment.course

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.snapco.techlife.R
import com.snapco.techlife.data.model.course.Course
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class AddCourse : AppCompatActivity() {
    private lateinit var viewModel: CourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        viewModel = ViewModelProvider(this).get(CourseViewModel::class.java)

        val txtCourseName = findViewById<EditText>(R.id.edtCourseName)
        val txtCoursePrice = findViewById<EditText>(R.id.edtCoursePrice)
        val txtCourseDuration = findViewById<EditText>(R.id.edtCourseDuration)
        val txtCourseDate = findViewById<EditText>(R.id.edtCourseDate)

        findViewById<Button>(R.id.btnSaveCourse).setOnClickListener {
            val course = Course(
                id = UUID.randomUUID().toString(),
                name = txtCourseName.text.toString(),
                price = txtCoursePrice.text.toString(),
                duration = txtCourseDuration.text.toString(),
                date = txtCourseDate.text.toString()
            )

            viewModel.addCourse(course)
        }

        viewModel.isCourseAdded.observe(this, Observer { isAdded ->
            if (isAdded) {
                Toast.makeText(this, "Thêm khóa học thành công!", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }
}
