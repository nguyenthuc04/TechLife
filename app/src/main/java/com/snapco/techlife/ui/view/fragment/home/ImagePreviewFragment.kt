package com.snapco.techlife.ui.view.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.snapco.techlife.R
import com.snapco.techlife.databinding.FragmentImagePreviewBinding
import com.snapco.techlife.ui.view.adapter.ImagePreviewAdapter

class ImagePreviewFragment : Fragment() {

    private lateinit var binding: FragmentImagePreviewBinding
    private lateinit var images: List<String>
    private var startPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Sử dụng ViewBinding để inflating layout
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


        binding.viewPager.adapter = ImagePreviewAdapter(images)
        binding.viewPager.setCurrentItem(startPosition, false)


        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
