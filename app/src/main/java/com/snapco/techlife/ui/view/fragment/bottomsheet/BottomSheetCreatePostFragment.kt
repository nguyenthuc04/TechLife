package com.snapco.techlife.ui.view.fragment.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snapco.techlife.databinding.FragmentBottomSheetCreatePostBinding
import com.snapco.techlife.ui.view.activity.home.CreatePostActivity

class BottomSheetCreatePostFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetCreatePostBinding
    private var listener: OnImageClickListener? = null

    interface OnImageClickListener {
        fun onImageView6Click()

        fun onImageView7Click()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnImageClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnImageClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomSheetCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView6.setOnClickListener {
            listener?.onImageView6Click()
        }
        binding.imageView7.setOnClickListener {
            listener?.onImageView7Click()
        }
        binding.textView28.setOnClickListener {
            listener?.onImageView6Click()
        }
        binding.textView29.setOnClickListener {
            listener?.onImageView7Click()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            val behavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.layoutParams.height = (resources.displayMetrics.heightPixels * 0.5).toInt()
            behavior.peekHeight = bottomSheet.layoutParams.height
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? CreatePostActivity)?.showBottomNavigationView()
    }
}
