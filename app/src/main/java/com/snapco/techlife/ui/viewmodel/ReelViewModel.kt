package com.snapco.techlife.ui.viewmodel

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snapco.techlife.R
import com.snapco.techlife.data.model.reel.CommentReel
import com.snapco.techlife.data.model.reel.Reel
import com.snapco.techlife.data.model.reel.ReelRetrofit
import com.snapco.techlife.databinding.ItemReelBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ReelViewModel: ViewModel() {
    private val _reelList = MutableLiveData<MutableList<Reel>?>()
    val reelList: MutableLiveData<MutableList<Reel>?> get() = _reelList

    //Fetch all reel for a specific user
    fun fetchReels(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ReelRetrofit.apiService.fetchUserReels(userId).execute()
                if (response.isSuccessful) {
                    _reelList.postValue(response.body()?.toMutableList())
                } else {
                    Log.e(
                        "SharedViewModel",
                        "Failed to fetch reels: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error fetching reels", e)
            }
        }
    }

    // Add a new reel, optionally with an image file
    fun createReel(reel: Reel, videoFile: File? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val reelData = Json.encodeToString(reel).toRequestBody()
                val filePart = videoFile?.let {
                    MultipartBody.Part.createFormData("file", it.name, it.asRequestBody())
                }
                val response =
                    ReelRetrofit.apiService.createReel(reelData.toString(), filePart).execute()

                if (response.isSuccessful) {
                    response.body()?.let {
                        _reelList.value?.add(0, it)
                        _reelList.postValue(_reelList.value)
                    }
                } else {
                    Log.e(
                        "ReelViewModel",
                        "Failed to create reel: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Error creating reel", e)
            }
        }
    }

    // Update an existing reel
    fun updateReel(updatedReel: Reel) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentList = _reelList.value ?: return@launch
            val index = currentList.indexOfFirst { it.reelId == updatedReel.reelId }
            if (index != -1) {
                currentList[index] = updatedReel
                _reelList.postValue(currentList)
            }
        }
    }

    // Delete a reel by ID
    fun deleteReel(reelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ReelRetrofit.apiService.deleteReel(reelId).execute()
                if (response.isSuccessful) {
                    _reelList.value?.removeIf { it.reelId == reelId }
                    _reelList.postValue(_reelList.value)
                }
            } catch (e: Exception) {
                Log.e("reelViewModel", "Error deleting reel", e)
            }
        }
    }

    fun likeReel(reelId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ReelRetrofit.apiService.likeReel(reelId, userId).execute()
                if (response.isSuccessful) {
                    // Update likes count in the post list
                    val currentList = _reelList.value
                    val postIndex = currentList?.indexOfFirst { it.reelId == reelId }
                    postIndex?.let {
                        currentList[it].likesReelCount += 1
                        _reelList.postValue(currentList)
                    }
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error liking post", e)
            }
        }
    }

    fun unlikePost(reelId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ReelRetrofit.apiService.unlikeReel(reelId, userId).execute()
                if (response.isSuccessful) {
                    val currentList = _reelList.value
                    val reelIndex = currentList?.indexOfFirst { it.reelId == reelId }
                    reelIndex?.let {
                        currentList[it].likesReelCount -= 1
                        _reelList.postValue(currentList)
                    }
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error unliking post", e)
            }
        }
    }

    fun addCommentReel(reelId: String, comment: CommentReel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ReelRetrofit.apiService.addCommentReel(reelId, comment).execute()
                if (response.isSuccessful) {
                    // Add new comment to the post
                    val currentList = _reelList.value
                    val reelIndex = currentList?.indexOfFirst { it.reelId == reelId }
                    reelIndex?.let {
                        currentList[it].commentsReelCount += 1
                        currentList[it].commentsReel = currentList[it].commentsReel + comment
                        _reelList.postValue(currentList)
                    }
                }
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Error adding comment", e)
            }
        }
    }

    fun fetchComments(reelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ReelRetrofit.apiService.getCommentsReel(reelId).execute()
                if (response.isSuccessful) {
                    val currentList = _reelList.value
                    val reelIndex = currentList?.indexOfFirst { it.reelId == reelId }
                    reelIndex?.let {
                        currentList[it].commentsReel = response.body() ?: listOf()
                        _reelList.postValue(currentList)
                    }
                }
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Error fetching comments", e)
            }
        }
    }



    inner class VideoViewHolder(private val binding: ItemReelBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isLiked = false // Biến trạng thái để theo dõi trạng thái like

        fun bindVideo(videoModel: Reel) {
            // Binding video
            binding.videoView.apply {
                setVideoPath(videoModel.videoUrl)
                setOnPreparedListener {
                    it.start()
                    it.isLooping = true
                }
                // Play/Pause
                setOnClickListener {
                    if (isPlaying) {
                        pause()
                        binding.btnPlay.visibility = View.VISIBLE
                    } else {
                        resume()
                        binding.btnPlay.visibility = View.GONE
                    }
                }
            }

            binding.userName.text = videoModel.userName
            binding.content.text = videoModel.caption
            Glide.with(binding.root.context) // Context hiện tại (Activity hoặc Fragment)
                .load(videoModel.userImageUrl) // URL của hình ảnh
                .into(binding.imgAvata)

            binding.btnFollow.setOnClickListener {
                Toast.makeText(binding.root.context, "Đã nhấn nút Follow", Toast.LENGTH_SHORT).show()
            }
            binding.btnHeart.setOnClickListener {
                isLiked = !isLiked // Chuyển đổi trạng thái
                if (isLiked) {
                    binding.btnHeart.setImageResource(R.drawable.ic_heart_red) // Thay đổi biểu tượng thành đỏ
                    Toast.makeText(binding.root.context, "Đã nhấn nút Like", Toast.LENGTH_SHORT).show()
                } else {
                    binding.btnHeart.setImageResource(R.drawable.ic_heart) // Thay đổi biểu tượng về trạng thái ban đầu
                    Toast.makeText(binding.root.context, "Đã bỏ Like", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnComment.setOnClickListener {
                showCommentDialog(binding.root.context)
            }

            binding.btnShare.setOnClickListener {
                Toast.makeText(binding.root.context, "Đã nhấn nút Share", Toast.LENGTH_SHORT).show()
            }
        }

        private fun showCommentDialog(context: Context) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_comment_reel)

            // Tính toán kích thước màn hình
            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

            // Chiều cao màn hình
            val screenHeight = displayMetrics.heightPixels

            // Thiết lập chiều cao của dialog chiếm 2/3 chiều cao màn hình
            val dialogHeight = (screenHeight * 2) / 3

            // Thiết lập kích thước cho dialog
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT // Chiều rộng toàn màn hình
            layoutParams.height = dialogHeight // Chiều cao 2/3 màn hình
            layoutParams.gravity = Gravity.BOTTOM // Đặt dialog ở phía dưới màn hình
            dialog.window?.attributes = layoutParams

            // Xử lý danh sách bình luận
//            val comments = mutableListOf<CommentModel>() // Danh sách bình luận
//            val commentAdapter = CommentAdapter(comments)

            val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewComments)
            recyclerView.layoutManager = LinearLayoutManager(context)
//            recyclerView.adapter = commentAdapter

            val editTextComment = dialog.findViewById<EditText>(R.id.editTextComment)
            dialog.findViewById<Button>(R.id.buttonSendComment).setOnClickListener {
                val newComment = editTextComment.text.toString()
//                if (newComment.isNotEmpty()) {
//                    comments.add(CommentModel(newComment))
//                    commentAdapter.notifyDataSetChanged()
//                    editTextComment.text.clear()
//                }
            }

            dialog.show()
        }
    }
}

class ReelAdapter(
    private val videoList: List<Reel>,
) : RecyclerView.Adapter<ReelViewModel.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewModel.VideoViewHolder {
        val binding = ItemReelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReelViewModel().VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReelViewModel.VideoViewHolder, position: Int) {
        holder.bindVideo(videoList[position])
    }

    override fun getItemCount(): Int = videoList.size
}
