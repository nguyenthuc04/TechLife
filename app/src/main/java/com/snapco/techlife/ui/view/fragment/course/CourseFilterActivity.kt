package com.snapco.techlife.ui.view.fragment.course

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityCourseBinding
import com.snapco.techlife.databinding.ActivityCourseFilterBinding
import com.snapco.techlife.databinding.ActivityMainBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.activity.MainActivity
import com.snapco.techlife.ui.viewmodel.objectdataholder.DataCheckMart
import java.time.LocalDate
import java.time.temporal.IsoFields

class CourseFilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseFilterBinding
    private lateinit var selectedOptions: MutableSet<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedOptions = DataCheckMart.getMultiplePreferences(this, "MyChecked").toMutableSet()
        if (selectedOptions.isEmpty()) {
            selectedOptions = mutableSetOf("Mức độ liên quan", "Bất kỳ", "Mọi thời điểm", "Tất cả")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
        }
        showChecked()

        // Xử lý sự kiện nút back
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbar.setBackgroundColor(this.getColor(R.color.white))

        binding.relevance.setOnClickListener {
            selectedOptions.add("Mức độ liên quan")
            selectedOptions.remove("Số lượng người tham gia nhiều nhất")
            selectedOptions.remove("Số lượng người tham gia ít nhất")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.mostParticipants.setOnClickListener {
            selectedOptions.remove("Mức độ liên quan")
            selectedOptions.add("Số lượng người tham gia nhiều nhất")
            selectedOptions.remove("Số lượng người tham gia ít nhất")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.leastParticipants.setOnClickListener {
            selectedOptions.remove("Mức độ liên quan")
            selectedOptions.remove("Số lượng người tham gia nhiều nhất")
            selectedOptions.add("Số lượng người tham gia ít nhất")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.anyDuration.setOnClickListener {
            selectedOptions.add("Bất kỳ")
            selectedOptions.remove("Dưới 2 tiếng")
            selectedOptions.remove("2 - 5 tiếng")
            selectedOptions.remove("Trên 5 tiếng")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.under2Hours.setOnClickListener {
            selectedOptions.remove("Bất kỳ")
            selectedOptions.add("Dưới 2 tiếng")
            selectedOptions.remove("2 - 5 tiếng")
            selectedOptions.remove("Trên 5 tiếng")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.id2To5.setOnClickListener {
            selectedOptions.remove("Bất kỳ")
            selectedOptions.remove("Dưới 2 tiếng")
            selectedOptions.add("2 - 5 tiếng")
            selectedOptions.remove("Trên 5 tiếng")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.over5Hours.setOnClickListener {
            selectedOptions.remove("Bất kỳ")
            selectedOptions.remove("Dưới 2 tiếng")
            selectedOptions.remove("2 - 5 tiếng")
            selectedOptions.add("Trên 5 tiếng")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.allTimes.setOnClickListener {
            selectedOptions.add("Mọi thời điểm")
            selectedOptions.remove("Hôm nay")
            selectedOptions.remove("Tuần này")
            selectedOptions.remove("Tháng này")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.today.setOnClickListener {
            selectedOptions.remove("Mọi thời điểm")
            selectedOptions.add("Hôm nay")
            selectedOptions.remove("Tuần này")
            selectedOptions.remove("Tháng này")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.thisWeek.setOnClickListener {
            selectedOptions.remove("Mọi thời điểm")
            selectedOptions.remove("Hôm nay")
            selectedOptions.add("Tuần này")
            selectedOptions.remove("Tháng này")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.thisMonth.setOnClickListener {
            selectedOptions.remove("Mọi thời điểm")
            selectedOptions.remove("Hôm nay")
            selectedOptions.remove("Tuần này")
            selectedOptions.add("Tháng này")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.alltype.setOnClickListener {
            selectedOptions.add("Tất cả")
            selectedOptions.remove("Cơ bản")
            selectedOptions.remove("Nâng cao")
            selectedOptions.remove("Chuyên môn hoá")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.basic.setOnClickListener {
            selectedOptions.remove("Tất cả")
            selectedOptions.add("Cơ bản")
            selectedOptions.remove("Nâng cao")
            selectedOptions.remove("Chuyên môn hoá")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.advanced.setOnClickListener {
            selectedOptions.remove("Tất cả")
            selectedOptions.remove("Cơ bản")
            selectedOptions.add("Nâng cao")
            selectedOptions.remove("Chuyên môn hoá")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

        binding.specialized.setOnClickListener {
            selectedOptions.remove("Tất cả")
            selectedOptions.remove("Cơ bản")
            selectedOptions.remove("Nâng cao")
            selectedOptions.add("Chuyên môn hoá")
            DataCheckMart.saveMultiplePreferences(this, "MyChecked", selectedOptions)
            showChecked()
        }

    }

    private fun showChecked () {
        val retrievedOptions = DataCheckMart.getMultiplePreferences(this, "MyChecked")
        Log.d("OPTIONS", "showChecked:ds = $retrievedOptions ")

        retrievedOptions.let {
            for (option in it) {
                if (option == "Mức độ liên quan") {
                    binding.checkmartMucdolienquan.visible()
                    binding.checkmartSlmax.gone()
                    binding.checkmartSlmin.gone()
                } else if (option == "Số lượng người tham gia nhiều nhất") {
                    binding.checkmartSlmax.visible()
                    binding.checkmartMucdolienquan.gone()
                    binding.checkmartSlmin.gone()
                } else if (option == "Số lượng người tham gia ít nhất") {
                    binding.checkmartSlmin.visible()
                    binding.checkmartSlmax.gone()
                    binding.checkmartMucdolienquan.gone()
                } else if (option == "Bất kỳ") {
                    binding.checkmartBatky.visible()
                    binding.checkmartMin2h.gone()
                    binding.checkmart2to5.gone()
                    binding.checkmartOver5.gone()
                } else if (option == "Dưới 2 tiếng") {
                    binding.checkmartMin2h.visible()
                    binding.checkmartBatky.gone()
                    binding.checkmart2to5.gone()
                    binding.checkmartOver5.gone()
                } else if (option == "2 - 5 tiếng") {
                    binding.checkmart2to5.visible()
                    binding.checkmartMin2h.gone()
                    binding.checkmartBatky.gone()
                    binding.checkmartOver5.gone()
                } else if (option == "Trên 5 tiếng") {
                    binding.checkmartOver5.visible()
                    binding.checkmartMin2h.gone()
                    binding.checkmartBatky.gone()
                    binding.checkmart2to5.gone()
                } else if (option == "Mọi thời điểm") {
                    binding.checkmartMoithoidiem.visible()
                    binding.checkmartToday.gone()
                    binding.checkmartThisweek.gone()
                    binding.checkmartThisMon.gone()
                } else if (option == "Hôm nay") {
                    binding.checkmartToday.visible()
                    binding.checkmartMoithoidiem.gone()
                    binding.checkmartThisweek.gone()
                    binding.checkmartThisMon.gone()
                } else if (option == "Tuần này") {
                    binding.checkmartThisweek.visible()
                    binding.checkmartToday.gone()
                    binding.checkmartMoithoidiem.gone()
                    binding.checkmartThisMon.gone()
                } else if (option == "Tháng này") {
                    binding.checkmartThisMon.visible()
                    binding.checkmartToday.gone()
                    binding.checkmartMoithoidiem.gone()
                    binding.checkmartThisweek.gone()
                } else if (option == "Tất cả") {
                    binding.checkmartAlltype.visible()
                    binding.checkmartBasic.gone()
                    binding.checkmartAdvanced.gone()
                    binding.checkmartSpecial.gone()
                } else if (option == "Cơ bản") {
                    binding.checkmartAlltype.gone()
                    binding.checkmartBasic.visible()
                    binding.checkmartAdvanced.gone()
                    binding.checkmartSpecial.gone()
                } else if (option == "Nâng cao") {
                    binding.checkmartAlltype.gone()
                    binding.checkmartAdvanced.visible()
                    binding.checkmartBasic.gone()
                    binding.checkmartSpecial.gone()
                } else if (option == "Chuyên môn hoá") {
                    binding.checkmartAlltype.gone()
                    binding.checkmartSpecial.visible()
                    binding.checkmartBasic.gone()
                    binding.checkmartAdvanced.gone()
                }


            }
        }
    }

}