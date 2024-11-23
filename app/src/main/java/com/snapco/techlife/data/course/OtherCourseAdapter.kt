package com.snapco.techlife.data.course

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.ui.viewmodel.CourseViewModel

class OtherCourseAdapter(
    private var courses: MutableList<Course>,
    private val viewModel: CourseViewModel,
) : RecyclerView.Adapter<OtherCourseAdapter.CourseViewHolder>() {
    inner class CourseViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        val courseName: TextView = view.findViewById(R.id.txtCourseName_other)
        val coursePrice: TextView = view.findViewById(R.id.txtCoursePrice_other)
        val courseDuration: TextView = view.findViewById(R.id.txtCourseDuration_other)
        val courseDate: TextView = view.findViewById(R.id.txtCourseDate_other)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_othercourse, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CourseViewHolder,
        position: Int,
    ) {
        val course = courses[position]
        holder.courseName.text = course.name
        holder.coursePrice.text = course.price
        holder.courseDuration.text = course.duration
        holder.courseDate.text = course.date

        val courseId = course.id
        if (courseId == null) {
            Log.e("OtherCourseAdapter", "Course ID is null for course at position $position")
        }
    }

    override fun getItemCount(): Int = courses.size

    fun updateCourses(newCourses: List<Course>) {
        Log.d("OtherCourseAdapter", "Updating courses with new data: $newCourses")
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }
}
