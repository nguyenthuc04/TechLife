package com.snapco.techlife.ui.view.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snapco.techlife.data.model.AddCommentReelRequest
import com.snapco.techlife.databinding.FragmentBottomSheetCommentBinding
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.view.adapter.CommentReelAdapter
import com.snapco.techlife.ui.viewmodel.ReelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class BottomSheetCommentReelFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetCommentBinding

    companion object {
        private const val ARG_POST_ID = "postId"

        fun newInstance(
            postId: String,
            userId: String,
        ): BottomSheetCommentReelFragment {
            val fragment = BottomSheetCommentReelFragment()
            val args = Bundle()
            args.putString(ARG_POST_ID, postId)
            args.putString("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBottomSheetCommentBinding.inflate(inflater, container, false)
        val postId = arguments?.getString(ARG_POST_ID) ?: return null
        val userId = arguments?.getString("userId") ?: ""
        val userImageUrl = UserDataHolder.getUserAvatar() ?: ""
        binding.imgAvatar.loadImage(userImageUrl)
        // Cài đặt RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CommentReelAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        // Gọi API để lấy danh sách comments
        fetchComments(postId, adapter)

        // Lắng nghe sự kiện nút gửi
        binding.btnSend.setOnClickListener {
            val commentText =
                binding.editText.text
                    .toString()
                    .trim()
            if (commentText.isNotEmpty()) {
                addComment(postId, commentText, adapter, userId)
            } else {
                binding.editText.error = "Nội dung bình luận không được để trống!"
            }
        }
        return binding.root
    }

    private fun addComment(
        postId: String,
        commentText: String,
        adapter: CommentReelAdapter,
        yourID: String,
    ) {
        val reelViewModel: ReelViewModel by viewModels({ requireActivity() })

        val userId = UserDataHolder.getUserId() ?: ""
        val userName = UserDataHolder.getUserName() ?: ""
        val userImageUrl = UserDataHolder.getUserAvatar() ?: ""

        val commentRequest =
            AddCommentReelRequest(
                userId = userId,
                userName = userName,
                userImageUrl = userImageUrl,
                text = commentText,
                yourID = yourID,
            )

        reelViewModel.addReelComment(postId, commentRequest)

        reelViewModel.addCommentReelResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.success) {
                    adapter.updateAccounts(it.reel?.comments ?: emptyList())
                    binding.editText.text.clear() // Xóa nội dung đã nhập
                } else {
                    // Hiển thị lỗi nếu có
                    binding.editText.error = it.message
                }
            }
        }
    }

    private fun fetchComments(
        postId: String,
        adapter: CommentReelAdapter,
    ) {
        val reelViewModel: ReelViewModel by viewModels({ requireActivity() })

        reelViewModel.commentReelResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                it.comments?.let { it1 -> adapter.updateAccounts(it1) }
            }
        }

        reelViewModel.getReelComments(postId)
    }
}
