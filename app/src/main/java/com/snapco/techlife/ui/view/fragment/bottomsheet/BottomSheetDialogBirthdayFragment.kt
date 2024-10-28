package com.snapco.techlife.ui.view.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snapco.techlife.databinding.BottomSheetDialogFragmentBinding
import com.snapco.techlife.ui.view.activity.signup.BottomSheetListener
import java.util.Calendar

class BottomSheetDialogBirthdayFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDialogFragmentBinding
    var listener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = BottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPickers()
    }

    private fun setupNumberPickers() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        binding.dayPicker.apply {
            minValue = 1
            maxValue = 31
        }
        binding.monthPicker.apply {
            minValue = 1
            maxValue = 12
            displayedValues = Array(12) { i -> "thg ${i + 1}" }
        }
        binding.yearPicker.apply {
            minValue = 1900
            maxValue = currentYear
        }

        val valueChangeListener =
            NumberPicker.OnValueChangeListener { _, _, _ ->
                updateDateAndAge(calendar, currentYear)
            }
        binding.dayPicker.setOnValueChangedListener(valueChangeListener)
        binding.monthPicker.setOnValueChangedListener(valueChangeListener)
        binding.yearPicker.setOnValueChangedListener(valueChangeListener)
    }

    private fun updateDateAndAge(
        calendar: Calendar,
        currentYear: Int,
    ) {
        val day = binding.dayPicker.value
        val month = binding.monthPicker.value
        val year = binding.yearPicker.value
        val formattedDate = String.format("%02d/%02d/%04d", day, month, year)
        val birthdayDate = String.format(String.format("ngày %02d tháng %02d, %04d", day, month, year))
        val selectedCalendar =
            Calendar.getInstance().apply {
                set(year, month - 1, day)
            }
        var age = currentYear - selectedCalendar.get(Calendar.YEAR)
        if (calendar.get(Calendar.DAY_OF_YEAR) < selectedCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        listener?.onDateSet(formattedDate, birthdayDate, age)
    }
}
