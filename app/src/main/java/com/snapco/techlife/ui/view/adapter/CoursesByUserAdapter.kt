package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.databinding.ItemCoursesByUserBinding
import com.snapco.techlife.extensions.formatPrice
import com.snapco.techlife.extensions.loadImage

class CoursesByUserAdapter(
    private var courses: MutableList<Course>,
    private val onCourseActionListener: OnCoursebyUserActionListener?,
) : RecyclerView.Adapter<CoursesByUserAdapter.CourseViewHolder>() {
    inner class CourseViewHolder(
        private val binding: ItemCoursesByUserBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course) {
            binding.courseImage.loadImage(course.imageUrl)
            binding.courseName.text = course.name
            binding.courseDateStart.text = "Ngày bắt đầu: ${course.startDate}"
            binding.courseDateEnd.text = "đến: ${course.endDate}"
            binding.coursePrice.text = course.price.toInt().formatPrice()
            binding.courseDuration.text = "Thời lượng: ${course.duration}"
            binding.courseDescription.text = course.describe
            binding.mentorImage.loadImage(course.userImageUrl)
            binding.mentorName.text = course.userName
            binding.mentorPhoneNumber.text = "Liên hệ: ${course.phoneNumber}"
            course.userId?.let { it1 -> binding.NextMess.setOnClickListener { onCourseActionListener?.clickMes(it1) } }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CourseViewHolder {
        val binding =
            ItemCoursesByUserBinding.inflate(
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

    interface OnCoursebyUserActionListener {
        fun clickMes(it: String)
    }
}
