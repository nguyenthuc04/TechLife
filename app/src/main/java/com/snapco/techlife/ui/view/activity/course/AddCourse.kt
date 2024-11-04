package com.snapco.techlife.ui.view.activity.course

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.snapco.techlife.R
import com.snapco.techlife.data.model.course.Course
import com.snapco.techlife.ui.viewmodel.CourseViewModel
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
            val name = txtCourseName.text.toString().trim()
            val price = txtCoursePrice.text.toString().trim()
            val duration = txtCourseDuration.text.toString().trim()
            val date = txtCourseDate.text.toString().trim()

            // Xác thực dữ liệu
            when {
                name.isEmpty() -> {
                    txtCourseName.error = "Tên khóa học không được để trống"
                    txtCourseName.requestFocus()
                }
                price.isEmpty() -> {
                    txtCoursePrice.error = "Giá khóa học không được để trống"
                    txtCoursePrice.requestFocus()
                }
                duration.isEmpty() -> {
                    txtCourseDuration.error = "Thời gian khóa học không được để trống"
                    txtCourseDuration.requestFocus()
                }
                date.isEmpty() -> {
                    txtCourseDate.error = "Ngày tạo không được để trống"
                    txtCourseDate.requestFocus()
                }
                else -> {
                    // Nếu dữ liệu hợp lệ, tạo và thêm khóa học mới
                    val course = Course(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        price = price,
                        duration = duration,
                        date = date
                    )

                    viewModel.addCourse(course)
                }
            }
        }

        viewModel.isCourseAdded.observe(this, Observer { isAdded ->
            if (isAdded) {
                Toast.makeText(this, "Thêm khóa học thành công!", Toast.LENGTH_SHORT).show()
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }
}
