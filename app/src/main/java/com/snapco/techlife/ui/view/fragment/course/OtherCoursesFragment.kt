package com.snapco.techlife.ui.view.fragment.course

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.data.model.UpdateCourseRequest
import com.snapco.techlife.data.model.UserCourse
import com.snapco.techlife.databinding.FragmentOtherCoursesBinding
import com.snapco.techlife.extensions.replaceFragment
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.adapter.OtherCourseAdapter
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.SearchViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.DataCheckMart
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import io.getstream.chat.android.models.Filters
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields

class OtherCoursesFragment : Fragment() {
    private val courseViewModel: CourseViewModel by viewModels()
    private val courseActivityViewModel: CourseViewModel by activityViewModels()

    private lateinit var binding: FragmentOtherCoursesBinding
    private lateinit var otherCourseAdapter: OtherCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentOtherCoursesBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter với callback xử lý nút
        otherCourseAdapter = OtherCourseAdapter(
            mutableListOf(),
            onItemClickListener = { course ->
                // Xử lý khi bấm vào item
                courseActivityViewModel.setCours(course)
                replaceFragment(CourseDetailsFragment())
            },
        )

        binding.imgFilter.setOnClickListener {
            startActivity<CourseFilterActivity>()
        }

        binding.recyclerView.adapter = otherCourseAdapter

        courseViewModel.getListCourses()

        courseViewModel.courses.observe(viewLifecycleOwner) { courseList ->
//            otherCourseAdapter.updateCourses(courseList)
            getDataFilter(courseList)
        }

        binding.edtSearchCourse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                courseViewModel.searchCourses(query)
                courseViewModel.searchCourses.observe(viewLifecycleOwner) { courseList ->
                    Log.d("FIx123", "onTextChanged: list = $courseList")
                    getDataFilter(courseList)
                }
                if(query.isEmpty()){
                    courseViewModel.getListCourses()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return binding.root
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

    fun getDataFilter(list: List<Course>) {
        Log.d("datal", "onTextChanged: list = $list ")
        val retrievedOptions = DataCheckMart.getMultiplePreferences(requireContext(), "MyChecked")
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        retrievedOptions?.let {
            var filteredCourses = list // Khởi tạo danh sách gốc

            for (option in it) {
                when (option) {
                    "Mức độ liên quan" -> {
                        // Không thay đổi danh sách
                    }
                    "Số lượng người tham gia nhiều nhất" -> {
                        // Sắp xếp từ cao xuống thấp
                        filteredCourses = filteredCourses.sortedByDescending { it.quantity }
                    }
                    "Số lượng người tham gia ít nhất" -> {
                        // Sắp xếp từ thấp lên cao
                        filteredCourses = filteredCourses.sortedBy { it.quantity }
                    }
                    "Bất kỳ" -> {
                        // Không lọc gì thêm
                    }
                    "Dưới 2 tiếng" -> {
                        filteredCourses = filteredCourses.filter { it.duration.toInt() < 2 }
                    }
                    "2 - 5 tiếng" -> {
                        filteredCourses = filteredCourses.filter { it.duration.toInt() in 2..5 }
                    }
                    "Trên 5 tiếng" -> {
                        filteredCourses = filteredCourses.filter { it.duration.toInt() > 5 }
                    }
                    "Mọi thời điểm" -> {
                        // Không lọc thời gian
                    }
                    "Hôm nay" -> {
                        val today = LocalDate.now()
                        filteredCourses = filteredCourses.filter {
                            val startDate = LocalDate.parse(it.startDate, dateFormatter)
                            startDate.isEqual(today)
                        }
                    }
                    "Tuần này" -> {
                        val today = LocalDate.now()
                        val weekOfYearToday = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                        filteredCourses = filteredCourses.filter {
                            val startDate = LocalDate.parse(it.startDate, dateFormatter)
                            val weekOfYearStart = startDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                            weekOfYearStart == weekOfYearToday
                        }
                    }
                    "Tháng này" -> {
                        val today = LocalDate.now()
                        val monthToday = today.monthValue
                        filteredCourses = filteredCourses.filter {
                            val startDate = LocalDate.parse(it.startDate, dateFormatter)
                            startDate.monthValue == monthToday
                        }
                    }
                    "Tất cả" -> {
                        // Không lọc
                    }
                    "Cơ bản" -> {
                        filteredCourses = filteredCourses.filter { it.type == "Cơ bản" }
                    }
                    "Nâng cao" -> {
                        filteredCourses = filteredCourses.filter { it.type == "Nâng cao" }
                    }
                    "Chuyên môn hoá" -> {
                        filteredCourses = filteredCourses.filter { it.type == "Chuyên môn hoá" }
                    }
                }
            }

            // Cập nhật danh sách sau khi lọc/sắp xếp
            otherCourseAdapter.updateCourses(filteredCourses)
        }
    }


    override fun onResume() {
        courseViewModel.getListCourses()
        courseViewModel.courses.observe(viewLifecycleOwner) { courseList ->
            getDataFilter(courseList) // Áp dụng các bộ lọc cho danh sách khóa học
        }
        super.onResume()
    }


}
