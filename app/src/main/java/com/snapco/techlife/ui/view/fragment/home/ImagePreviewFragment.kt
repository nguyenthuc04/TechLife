package com.snapco.techlife.ui.view.fragment.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.snapco.techlife.R
import com.snapco.techlife.databinding.FragmentImagePreviewBinding
import com.snapco.techlife.ui.view.adapter.ImagePreviewAdapter

class ImagePreviewDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentImagePreviewBinding
    private lateinit var images: List<String>
    private var startPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Sử dụng ViewBinding
        binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy dữ liệu từ arguments
        arguments?.let {
            images = it.getStringArrayList("images") ?: emptyList()
            startPosition = it.getInt("startPosition", 0)
        }

        // Kiểm tra dữ liệu trước khi thiết lập adapter
        if (images.isNotEmpty()) {
            binding.viewPager.adapter = ImagePreviewAdapter(images)
            binding.viewPager.setCurrentItem(startPosition, false)
        }

        // Xử lý nút back
        binding.backButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.FullScreenDialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK)) // Nền đen hoàn toàn

        // Thay đổi màu status bar khi mở dialog
        dialog.window?.statusBarColor = Color.BLACK

        // Cho phép đóng dialog khi nhấn ngoài vùng của dialog
        dialog.setCanceledOnTouchOutside(true)

        return dialog
    }
}

