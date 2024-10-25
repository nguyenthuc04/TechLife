package com.snapco.techlife.ui.view.fragment.reels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.snapco.techlife.R
import com.snapco.techlife.data.model.ReelModel
import com.snapco.techlife.ui.viewmodel.ReelAdapter

class ReelsFragment : Fragment() {

    // Thay đổi các tham số nếu cần
    private var param1: String? = null
    private var param2: String? = null

    // RecyclerView
    private lateinit var viewPager: ViewPager2
    private lateinit var reelAdapter: ReelAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewPager2
        viewPager = view.findViewById(R.id.view_pager)

        // Tạo danh sách video mẫu
        val videoList = listOf(
            ReelModel("https://www.youtube.com/shorts/m2s1HMqwb8o?feature=share", "Người dùng 1", "Nội dung video 1"),
            ReelModel("https://www.youtube.com/shorts/YBA_ERIN1UU?feature=share", "Người dùng 2", "Nội dung video 2"),
            ReelModel("https://www.youtube.com/shorts/lrYbkgaW8xI?feature=share", "Người dùng 3", "Nội dung video 3")
        )

        // Khởi tạo adapter và thiết lập cho ViewPager2
        reelAdapter = ReelAdapter(videoList)
        viewPager.adapter = reelAdapter
    }

}
