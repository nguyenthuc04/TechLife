package com.snapco.techlife.ui.view.fragment.course

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.databinding.FragmentOtherCoursesBinding
import com.snapco.techlife.extensions.replaceFragment
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.course.CoursesByUserActivity
import com.snapco.techlife.ui.view.adapter.OtherCourseAdapter
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.DataCheckMart
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields

class OtherCoursesFragment : Fragment() {
    private val courseViewModel: CourseViewModel by viewModels()
    private val courseActivityViewModel: CourseViewModel by activityViewModels()

    private lateinit var binding: FragmentOtherCoursesBinding
    private lateinit var basicCourseAdapter: OtherCourseAdapter
    private lateinit var advancedCourseAdapter: OtherCourseAdapter
    private lateinit var specializedCourseAdapter: OtherCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOtherCoursesBinding.inflate(inflater, container, false)

        setupRecyclerViews()

        courseViewModel.getListCourses()
        courseViewModel.courses.observe(viewLifecycleOwner) { courseList ->
            filterAndDisplayCourses(courseList)
        }
        binding.btnList.setOnClickListener {
            startActivity<CoursesByUserActivity>()
        }
        binding.edtSearchCourse.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    val query = s.toString().trim()
                    courseViewModel.searchCourses(query)
                    courseViewModel.searchCourses.observe(viewLifecycleOwner) { courseList ->
                        filterAndDisplayCourses(courseList)
                    }
                    if (query.isEmpty()) {
                        courseViewModel.getListCourses()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            },
        )

        binding.imgFilter.setOnClickListener {
            startActivity<CourseFilterActivity>()
        }

        return binding.root
    }

    private fun setupRecyclerViews() {
        basicCourseAdapter =
            OtherCourseAdapter(mutableListOf()) { course -> onCourseClicked(course) }
        advancedCourseAdapter =
            OtherCourseAdapter(mutableListOf()) { course -> onCourseClicked(course) }
        specializedCourseAdapter =
            OtherCourseAdapter(mutableListOf()) { course -> onCourseClicked(course) }

        binding.rvBasicCourses.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = basicCourseAdapter
        }

        binding.rvAdvancedCourses.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = advancedCourseAdapter
        }

        binding.rvSpecializedCourses.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = specializedCourseAdapter
        }
    }

    private fun filterAndDisplayCourses(courseList: List<Course>) {
        val retrievedOptions = DataCheckMart.getMultiplePreferences(requireContext(), "MyChecked")
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        retrievedOptions?.let {
            var filteredCourses =
                courseList.filter { course ->
                    var matches = true

                    for (option in it) {
                        when (option) {
                            "Mức độ liên quan", "Bất kỳ", "Mọi thời điểm", "Tất cả" -> matches
                            "Dưới 2 tiếng" ->
                                matches =
                                    matches &&
                                    course.duration.replace(Regex("[^\\d]"), "").toInt() < 2

                            "2 - 5 tiếng" ->
                                matches =
                                    matches &&
                                    course.duration.replace(Regex("[^\\d]"), "").toInt() in 2..5

                            "Trên 5 tiếng" ->
                                matches =
                                    matches &&
                                    course.duration.replace(Regex("[^\\d]"), "").toInt() > 5

                            "Hôm nay" -> {
                                val today = LocalDate.now()
                                val startDate = LocalDate.parse(course.startDate, dateFormatter)
                                matches = matches && startDate.isEqual(today)
                            }

                            "Tuần này" -> {
                                val today = LocalDate.now()
                                val weekOfYearToday = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                                val startDate = LocalDate.parse(course.startDate, dateFormatter)
                                val weekOfYearStart = startDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                                matches = matches && weekOfYearStart == weekOfYearToday
                            }

                            "Tháng này" -> {
                                val today = LocalDate.now()
                                val monthToday = today.monthValue
                                val startDate = LocalDate.parse(course.startDate, dateFormatter)
                                val monthStart = startDate.monthValue
                                matches = matches && monthStart == monthToday
                            }

                            "Cơ bản" -> matches = matches && course.type == "Cơ bản"
                            "Nâng cao" -> matches = matches && course.type == "Nâng cao"
                            "Chuyên môn hoá" -> matches = matches && course.type == "Chuyên môn hoá"
                        }
                    }
                    matches
                }

            // Áp dụng sắp xếp
            filteredCourses =
                when {
                    it.contains("Số lượng người tham gia nhiều nhất") ->
                        filteredCourses.sortedByDescending { it.quantity }
                    it.contains("Số lượng người tham gia ít nhất") ->
                        filteredCourses.sortedBy { it.quantity }
                    else -> filteredCourses
                }

            // Hiển thị hoặc ẩn thông báo không có khóa học
            if (filteredCourses.isEmpty()) {
                binding.tvNoCourses.visibility = View.VISIBLE
            } else {
                binding.tvNoCourses.visibility = View.GONE
            }

            // Chia loại khóa học và cập nhật giao diện
            val basicCourses = filteredCourses.filter { it.type == "Cơ bản" }
            val advancedCourses = filteredCourses.filter { it.type == "Nâng cao" }
            val specializedCourses = filteredCourses.filter { it.type == "Chuyên môn hoá" }

            basicCourseAdapter.updateCourses(basicCourses)
            advancedCourseAdapter.updateCourses(advancedCourses)
            specializedCourseAdapter.updateCourses(specializedCourses)

            binding.tvBasicCourses.visibility =
                if (basicCourses.isEmpty()) View.GONE else View.VISIBLE
            binding.rvBasicCourses.visibility =
                if (basicCourses.isEmpty()) View.GONE else View.VISIBLE
            binding.tvAdvancedCourses.visibility =
                if (advancedCourses.isEmpty()) View.GONE else View.VISIBLE
            binding.rvAdvancedCourses.visibility =
                if (advancedCourses.isEmpty()) View.GONE else View.VISIBLE
            binding.tvSpecializedCourses.visibility =
                if (specializedCourses.isEmpty()) View.GONE else View.VISIBLE
            binding.rvSpecializedCourses.visibility =
                if (specializedCourses.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun onCourseClicked(course: Course) {
        courseActivityViewModel.setCours(course)
        replaceFragment(CourseDetailsFragment())
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_COURSE && resultCode == AppCompatActivity.RESULT_OK) {
            courseViewModel.getListCourses()
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_COURSE = 1
    }

    override fun onResume() {
        courseViewModel.getListCourses()
        courseViewModel.courses.observe(viewLifecycleOwner) { courseList ->
            filterAndDisplayCourses(courseList)
        }
        super.onResume()
    }
}
