package com.snapco.techlife.data.model.course

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.ui.view.activity.course.EditCourse
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class CourseAdapter(
    private var courses: MutableList<Course>, // MutableList để có thể cập nhật dữ liệu
    private val viewModel: CourseViewModel
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    // ViewHolder class
    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courseName: TextView = view.findViewById(R.id.txtCourseName)
        val coursePrice: TextView = view.findViewById(R.id.txtCoursePrice)
        val courseDuration: TextView = view.findViewById(R.id.txtCourseDuration)
        val courseDate: TextView = view.findViewById(R.id.txtCourseDate)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteCourse)
        val btnEdit: Button = view.findViewById(R.id.btnEditCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.courseName.text = course.name
        holder.coursePrice.text = course.price
        holder.courseDuration.text = course.duration
        holder.courseDate.text = course.date

        val courseId = course.id
        if (courseId == null) {
            Log.e("CourseAdapter", "Course ID is null for course at position $position")
            // Bạn có thể hiển thị thông báo cho người dùng hoặc thực hiện hành động khác nếu cần
        }


        holder.btnDelete.setOnClickListener {
            courseId?.let { id ->
                viewModel.deleteCourse(id) // Đảm bảo id là ObjectId
            } ?: run {
                Toast.makeText(holder.itemView.context, "ID khóa học không hợp lệ!", Toast.LENGTH_SHORT).show()
            }
        }

        holder.btnEdit.setOnClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.activity_edit_course, null)

            // Khởi tạo dialog
            val dialog = AlertDialog.Builder(holder.itemView.context)
                .setView(dialogView)
                .setTitle("Chỉnh sửa khóa học")
                .setPositiveButton("Cập nhật", null) // Để chúng ta có thể xử lý logic sau
                .setNegativeButton("Hủy", null)
                .create()

            // Gán giá trị hiện tại vào EditText
            val edtCourseName: EditText = dialogView.findViewById(R.id.edtCourseName_ed)
            val edtCoursePrice: EditText = dialogView.findViewById(R.id.edtCoursePrice_ed)
            val edtCourseDuration: EditText = dialogView.findViewById(R.id.edtCourseDuration_ed)
            val edtCourseDate: EditText = dialogView.findViewById(R.id.edtCourseDate_ed)

            // Thiết lập giá trị cho EditText
            edtCourseName.setText(course.name)
            edtCoursePrice.setText(course.price)
            edtCourseDuration.setText(course.duration)
            edtCourseDate.setText(course.date) // Giả sử bạn có trường ngày trong Course

            // Xử lý nút Cập nhật
            dialog.setOnShowListener {
                val btnUpdate = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                btnUpdate.setOnClickListener {
                    val updatedCourse = Course(
                        id = courseId, // giữ nguyên ID khóa học
                        name = edtCourseName.text.toString(),
                        price = edtCoursePrice.text.toString(),
                        duration = edtCourseDuration.text.toString(),
                        date = edtCourseDate.text.toString() // Cập nhật thêm trường ngày nếu cần
                    )
                    viewModel.updateCourse(updatedCourse)
                    dialog.dismiss() // Đóng dialog
                }
            }

            dialog.show()
        }
    }

    override fun getItemCount(): Int = courses.size


    fun updateCourses(newCourses: List<Course>) {
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }
}
