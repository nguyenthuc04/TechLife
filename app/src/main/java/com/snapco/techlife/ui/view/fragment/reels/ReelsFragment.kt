package com.snapco.techlife.ui.view.fragment.reels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.Timestamp
import com.snapco.techlife.R
import com.snapco.techlife.data.model.reel.Reel
import com.snapco.techlife.ui.viewmodel.ReelAdapter

class ReelsFragment : Fragment() {

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
            Reel(
                reelId = "1",
                caption = "Video của người dùng 1",
                videoUrl = "https://www.youtube.com/shorts/m2s1HMqwb8o?feature=share",
                createdAt = Timestamp.now(),  // Thời gian hiện tại
                likesCount = "120",
                commentsCount = "5",
                userId = "user_1",
                userName = "Người dùng 1",
                userImageUrl = "https://www.google.com.vn/url?sa=i&url=https%3A%2F%2Fmuctim.tuoitre.vn%2Ffacebook-avatar.html&psig=AOvVaw31bqWYwWc2f8-1KYB4bCiu&ust=1731418569789000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCJjYtvmy1IkDFQAAAAAdAAAAABAE",
                isLiked = true,
                isOwnPost = false
            ),
            Reel(
                reelId = "2",
                caption = "Video của người dùng 2",
                videoUrl = "https://www.youtube.com/shorts/YBA_ERIN1UU?feature=share",
                createdAt = Timestamp.now(),  // Thời gian hiện tại
                likesCount = "250",
                commentsCount = "15",
                userId = "user_2",
                userName = "Người dùng 2",
                userImageUrl = "https://www.google.com.vn/url?sa=i&url=https%3A%2F%2Fmuctim.tuoitre.vn%2Ffacebook-avatar.html&psig=AOvVaw31bqWYwWc2f8-1KYB4bCiu&ust=1731418569789000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCJjYtvmy1IkDFQAAAAAdAAAAABAE",
                isLiked = false,
                isOwnPost = true
            ),
            Reel(
                reelId = "3",
                caption = "Video của người dùng 3",
                videoUrl = "https://www.youtube.com/shorts/lrYbkgaW8xI?feature=share",
                createdAt = Timestamp.now(),  // Thời gian hiện tại
                likesCount = "75",
                commentsCount = "10",
                userId = "user_3",
                userName = "Người dùng 3",
                userImageUrl = "https://www.google.com.vn/url?sa=i&url=https%3A%2F%2Fmuctim.tuoitre.vn%2Ffacebook-avatar.html&psig=AOvVaw31bqWYwWc2f8-1KYB4bCiu&ust=1731418569789000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCJjYtvmy1IkDFQAAAAAdAAAAABAE",
                isLiked = true,
                isOwnPost = false
            )
        )



        // Khởi tạo adapter với callback cho btnCamera
        reelAdapter = ReelAdapter(videoList)
        // Thiết lập adapter cho ViewPager2
        viewPager.adapter = reelAdapter
    }
}
