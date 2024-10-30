package com.snapco.techlife.data.model.course

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
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

        // Xóa khóa học
        holder.btnDelete.setOnClickListener {
//            viewModel.deleteCourse(course.id)
//            courses.removeAt(position)  // Xóa khóa học khỏi danh sách
//            notifyItemRemoved(position) // Cập nhật RecyclerView khi xóa khóa học
        }

        // Sửa khóa học
        holder.btnEdit.setOnClickListener {
//            val intent = Intent(holder.itemView.context, EditCourse::class.java)
//            intent.putExtra("courseId", course.id)
//            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = courses.size

    // Phương thức cập nhật danh sách khóa học
    fun updateCourses(newCourses: List<Course>) {
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged() // Cập nhật RecyclerView khi có thay đổi trong danh sách
    }
}
