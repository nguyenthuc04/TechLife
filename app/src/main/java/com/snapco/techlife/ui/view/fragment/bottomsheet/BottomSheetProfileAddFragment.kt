package com.snapco.techlife.ui.view.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snapco.techlife.R
import com.snapco.techlife.databinding.BottomSheetProfilePlusBinding

class BottomSheetProfileAddFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetProfilePlusBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = BottomSheetProfilePlusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView28.setOnClickListener {
            // Reels
        }
        binding.textView29.setOnClickListener {
            // Bài viết
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setBackgroundDrawableResource(R.color.dim_background)
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            val behavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.layoutParams.height = (resources.displayMetrics.heightPixels * 0.5).toInt()
            behavior.peekHeight = bottomSheet.layoutParams.height
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}