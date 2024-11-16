package com.snapco.techlife.ui.view.fragment.reels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.Timestamp
import com.snapco.techlife.R
import com.snapco.techlife.data.model.reel.CommentReel
import com.snapco.techlife.data.model.reel.LikeReel
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
                userId = "user_1",
                userName = "Người dùng 1",
                userImageUrl = "https://www.example.com/avatar1.png",
                caption = "Video của người dùng 1",
                videoUrl = "https://www.youtube.com/shorts/m2s1HMqwb8o?feature=share",
                likesReelCount = 120,
                commentsReelCount = 5,
                isLiked = true,
                likesReel = listOf(
                    LikeReel(likeReelId = "like_1", reelId = "1", userId = "user_2", likedReelAt = "2024-11-15T10:15:30Z"),
                    LikeReel(likeReelId = "like_2", reelId = "1", userId = "user_3", likedReelAt = "2024-11-15T10:20:30Z")
                ),
                commentsReel = listOf(
                    CommentReel(
                        commentReelId = "comment_1", reelId = "1", userId = "user_2",
                        userName = "Người dùng 2", userImageUrl = "https://www.example.com/avatar2.png",
                        commentReelText = "Video hay quá!", commentedReelAt = "2024-11-15T10:25:30Z"
                    ),
                    CommentReel(
                        commentReelId = "comment_2", reelId = "1", userId = "user_3",
                        userName = "Người dùng 3", userImageUrl = "https://www.example.com/avatar3.png",
                        commentReelText = "Thích video này!", commentedReelAt = "2024-11-15T10:30:30Z"
                    )
                )
            ),
            Reel(
                reelId = "2",
                userId = "user_2",
                userName = "Người dùng 2",
                userImageUrl = "https://www.example.com/avatar2.png",
                caption = "Video của người dùng 2",
                videoUrl = "https://www.youtube.com/shorts/YBA_ERIN1UU?feature=share",
                likesReelCount = 250,
                commentsReelCount = 15,
                isLiked = false,
                likesReel = listOf(
                    LikeReel(likeReelId = "like_3", reelId = "2", userId = "user_1", likedReelAt = "2024-11-15T11:00:30Z"),
                    LikeReel(likeReelId = "like_4", reelId = "2", userId = "user_3", likedReelAt = "2024-11-15T11:05:30Z")
                ),
                commentsReel = listOf(
                    CommentReel(
                        commentReelId = "comment_3", reelId = "2", userId = "user_1",
                        userName = "Người dùng 1", userImageUrl = "https://www.example.com/avatar1.png",
                        commentReelText = "Rất thú vị!", commentedReelAt = "2024-11-15T11:10:30Z"
                    ),
                    CommentReel(
                        commentReelId = "comment_4", reelId = "2", userId = "user_3",
                        userName = "Người dùng 3", userImageUrl = "https://www.example.com/avatar3.png",
                        commentReelText = "Video xuất sắc!", commentedReelAt = "2024-11-15T11:15:30Z"
                    )
                )
            ),
            Reel(
                reelId = "3",
                userId = "user_3",
                userName = "Người dùng 3",
                userImageUrl = "https://www.example.com/avatar3.png",
                caption = "Video của người dùng 3",
                videoUrl = "https://www.youtube.com/shorts/lrYbkgaW8xI?feature=share",
                likesReelCount = 75,
                commentsReelCount = 10,
                isLiked = true,
                likesReel = listOf(
                    LikeReel(likeReelId = "like_5", reelId = "3", userId = "user_1", likedReelAt = "2024-11-15T11:30:30Z"),
                    LikeReel(likeReelId = "like_6", reelId = "3", userId = "user_2", likedReelAt = "2024-11-15T11:35:30Z")
                ),
                commentsReel = listOf(
                    CommentReel(
                        commentReelId = "comment_5", reelId = "3", userId = "user_1",
                        userName = "Người dùng 1", userImageUrl = "https://www.example.com/avatar1.png",
                        commentReelText = "Thích video này lắm!", commentedReelAt = "2024-11-15T11:40:30Z"
                    ),
                    CommentReel(
                        commentReelId = "comment_6", reelId = "3", userId = "user_2",
                        userName = "Người dùng 2", userImageUrl = "https://www.example.com/avatar2.png",
                        commentReelText = "Video tuyệt vời!", commentedReelAt = "2024-11-15T11:45:30Z"
                    )
                )
            )
        )





        // Khởi tạo adapter với callback cho btnCamera
        reelAdapter = ReelAdapter(videoList)
        // Thiết lập adapter cho ViewPager2
        viewPager.adapter = reelAdapter
    }
}
