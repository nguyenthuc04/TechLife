package com.snapco.techlife.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.databinding.ItemMycourseBinding
import com.snapco.techlife.extensions.loadImage

class MyCourseAdapter(
    private var courses: List<Course>,
    private val onCourseActionListener: OnCourseActionListener?,
    private val onItemClickListener: (Course) -> Unit // Thêm callback cho sự kiện bấm vào item

) : RecyclerView.Adapter<MyCourseAdapter.CourseViewHolder>() {
    inner class CourseViewHolder(
        private val binding: ItemMycourseBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course) {
            binding.textView34.text = course.name
            binding.imageView11.loadImage(course.imageUrl)
            binding.imgAvatar.loadImage(course.userImageUrl)
            binding.textView20.text = course.userName
            binding.textView34.text = course.name
            binding.textView38.text = "Số lượng còn lại: ${course.quantity}"
            binding.imageView14.setOnClickListener {
                onCourseActionListener?.showPopupMenu(it, course, position)
            }

            binding.root.setOnClickListener {
                onItemClickListener(course) // Gọi callback khi bấm vào item
                Log.d("MyCourseAdapter", "Course clicked: $course")
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CourseViewHolder {
        val binding =
            ItemMycourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CourseViewHolder,
        position: Int,
    ) {
        val course = courses[position]
        holder.bind(course)
    }

    override fun getItemCount(): Int = courses.size

    fun updateCourses(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }

    interface OnCourseActionListener {
        fun showPopupMenu(
            it: View?,
            course: Course,
            position: Int,
        )
    }
}
