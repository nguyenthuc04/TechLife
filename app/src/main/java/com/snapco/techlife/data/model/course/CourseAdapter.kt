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
    private var courses: MutableList<Course>,
    private val viewModel: CourseViewModel
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courseName: TextView = view.findViewById(R.id.txtCourseName)
        val coursePrice: TextView = view.findViewById(R.id.txtCoursePrice)
        val courseDuration: TextView = view.findViewById(R.id.txtCourseDuration)
        val courseDate: TextView = view.findViewById(R.id.txtCourseDate)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteCourse)

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
        }


        holder.btnDelete.setOnClickListener {
            courseId?.let { id ->
                val deleteDialogBuilder = AlertDialog.Builder(holder.itemView.context)
                deleteDialogBuilder.setTitle("Xác nhận xóa")
                deleteDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa khóa học này không?")

                deleteDialogBuilder.setPositiveButton("Đồng ý") { dialog, _ ->
                    viewModel.deleteCourse(id)
                    dialog.dismiss()
                }

                deleteDialogBuilder.setNegativeButton("Hủy") { dialog, _ ->
                    dialog.dismiss()
                }

                val deleteDialog = deleteDialogBuilder.create()
                deleteDialog.show()
            } ?: run {
                Toast.makeText(holder.itemView.context, "ID khóa học không hợp lệ!", Toast.LENGTH_SHORT).show()
            }
        }


        holder.itemView.setOnLongClickListener {
            // Tạo dialog
            val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_edit_course, null)
            val edtCourseName = dialogView.findViewById<EditText>(R.id.edtCourseName_ed)
            val edtCoursePrice = dialogView.findViewById<EditText>(R.id.edtCoursePrice_ed)
            val edtCourseDuration = dialogView.findViewById<EditText>(R.id.edtCourseDuration_ed)
            val edtCourseDate = dialogView.findViewById<EditText>(R.id.edtCourseDate_ed)
            val btnSave = dialogView.findViewById<Button>(R.id.btnSaveCourse_ed)

            // Điền dữ liệu vào dialog
            edtCourseName.setText(course.name)
            edtCoursePrice.setText(course.price)
            edtCourseDuration.setText(course.duration)
            edtCourseDate.setText(course.date)

            // Tạo dialog
            val dialogBuilder = AlertDialog.Builder(holder.itemView.context)
            dialogBuilder.setView(dialogView)
            val dialog = dialogBuilder.create()
            dialog.show()

            // Khi bấm lưu
            btnSave.setOnClickListener {
                val name = edtCourseName.text.toString().trim()
                val price = edtCoursePrice.text.toString().trim()
                val duration = edtCourseDuration.text.toString().trim()
                val date = edtCourseDate.text.toString().trim()

                // Kiểm tra tính hợp lệ của dữ liệu
                when {
                    name.isEmpty() -> {
                        edtCourseName.error = "Tên khóa học không được để trống"
                        edtCourseName.requestFocus()
                    }
                    price.isEmpty() -> {
                        edtCoursePrice.error = "Giá khóa học không được để trống"
                        edtCoursePrice.requestFocus()
                    }
                    duration.isEmpty() -> {
                        edtCourseDuration.error = "Thời gian khóa học không được để trống"
                        edtCourseDuration.requestFocus()
                    }
                    date.isEmpty() -> {
                        edtCourseDate.error = "Ngày tạo không được để trống"
                        edtCourseDate.requestFocus()
                    }
                    else -> {
                        // Nếu tất cả dữ liệu hợp lệ, tiến hành cập nhật khóa học
                        val updatedCourse = Course(
                            id = course.id,
                            name = name,
                            price = price,
                            duration = duration,
                            date = date
                        )

                        // Gọi ViewModel để cập nhật khóa học
                        viewModel.updateCourse(updatedCourse)
                        dialog.dismiss()
                    }
                }
            }
            true // Trả về true để chỉ ra rằng sự kiện nhấn giữ đã được xử lý
        }


    }

    override fun getItemCount(): Int = courses.size


    fun updateCourses(newCourses: List<Course>) {
        Log.d("CourseAdapter", "Updating courses with new data: $newCourses")
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }
}
