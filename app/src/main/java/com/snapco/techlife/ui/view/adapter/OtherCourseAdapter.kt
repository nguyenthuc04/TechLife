package com.snapco.techlife.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.databinding.ItemOthercourseBinding
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class OtherCourseAdapter(
    private var courses: MutableList<Course>,
    private val onItemClickListener: (Course) -> Unit, // Thêm callback cho sự kiện bấm vào item
) : RecyclerView.Adapter<OtherCourseAdapter.CourseViewHolder>() {
    inner class CourseViewHolder(
        private val binding: ItemOthercourseBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course) {
            binding.textView34.text = course.name
            binding.imageView11.loadImage(course.imageUrl)
            binding.textView20.text = course.userName
            binding.textView34.text = course.name

            binding.root.setOnClickListener {
                onItemClickListener(course) // Gọi callback khi bấm vào item
                Log.d("OtherCourseAdapter", "Course clicked: $course")
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CourseViewHolder {
        val binding =
            ItemOthercourseBinding.inflate(
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
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }
}
