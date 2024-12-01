package com.snapco.techlife.ui.view.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snapco.techlife.R
import com.snapco.techlife.databinding.BottomSheetProfilePlusBinding
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.course.AddCourse
import com.snapco.techlife.ui.view.activity.home.CreatePostActivity
import com.snapco.techlife.ui.view.activity.home.CreateReelActivity
import com.snapco.techlife.ui.view.activity.premium.PremiumActivity
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder

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
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            if (response.user.accountType == "mentor") {
                binding.textView30.text = "Gia hạn"
            } else {
                binding.textView30.text = "Nâng cấp"
            }
        }
        binding.textView28.setOnClickListener {
            startActivity<CreateReelActivity>()
        }
        binding.textView29.setOnClickListener {
            startActivity<CreatePostActivity>()
        }
        binding.textView30.setOnClickListener {
            startActivity<PremiumActivity>()
        }
        binding.textView31.setOnClickListener {
            startActivity<AddCourse>()
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
