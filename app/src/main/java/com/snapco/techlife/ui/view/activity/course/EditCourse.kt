package com.snapco.techlife.ui.view.activity.course

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.snapco.techlife.R
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class EditCourse : AppCompatActivity() {
    private lateinit var viewModel: CourseViewModel
    private lateinit var courseId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_course)

        viewModel = ViewModelProvider(this).get(CourseViewModel::class.java)
        courseId = intent.getStringExtra("courseId") ?: return

        val edtCourseName = findViewById<EditText>(R.id.edtCourseName_ed)
        val edtCoursePrice = findViewById<EditText>(R.id.edtCoursePrice_ed)
        val edtCourseDuration = findViewById<EditText>(R.id.edtCourseDuration_ed)
        val edtCourseDate = findViewById<EditText>(R.id.edtCourseDate_ed)

        viewModel.getCourseById(courseId)

        viewModel.course.observe(this, Observer { course ->
            if (course != null) {
                edtCourseName.setText(course.name)
                edtCoursePrice.setText(course.price)
                edtCourseDuration.setText(course.duration)
                edtCourseDate.setText(course.date)
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin khóa học!", Toast.LENGTH_SHORT).show()
            }
        })

//        findViewById<Button>(R.id.btnSaveCourse_ed).setOnClickListener {
//            // Kiểm tra xem người dùng đã nhập đầy đủ thông tin chưa
//            if (edtCourseName.text.isEmpty() || edtCoursePrice.text.isEmpty() ||
//                edtCourseDuration.text.isEmpty() || edtCourseDate.text.isEmpty()) {
//                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val updatedCourse = Course(
//                id = courseId,
//                name = edtCourseName.text.toString(),
//                price = edtCoursePrice.text.toString(),
//                duration = edtCourseDuration.text.toString(),
//                date = edtCourseDate.text.toString()
//            )
//
//            viewModel.updateCourse(updatedCourse)
//        }


        viewModel.isCourseUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated) {
                Toast.makeText(this, "Cập nhật khóa học thành công!", Toast.LENGTH_SHORT).show()
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
