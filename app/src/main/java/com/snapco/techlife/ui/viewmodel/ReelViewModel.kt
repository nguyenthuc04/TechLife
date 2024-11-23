package com.snapco.techlife.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.model.AddCommentReelRequest
import com.snapco.techlife.data.model.AddCommentReelResponse
import com.snapco.techlife.data.model.CreateReelRequest
import com.snapco.techlife.data.model.CreateReelResponse
import com.snapco.techlife.data.model.GetCommentReelResponse
import com.snapco.techlife.data.model.LikeReelResponse
import com.snapco.techlife.data.model.Reel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReelViewModel : ViewModel() {
    private val _createReelResponse = MutableLiveData<CreateReelResponse>()
    val createReelResponse: LiveData<CreateReelResponse> get() = _createReelResponse

    private val _reel = MutableLiveData<List<Reel>>()
    val reel: LiveData<List<Reel>> get() = _reel

    private val _likeReelResponse = MutableLiveData<LikeReelResponse>()
    val likeReelResponse: LiveData<LikeReelResponse> get() = _likeReelResponse

    private val _commentReelResponse = MutableLiveData<GetCommentReelResponse>()
    val commentReelResponse: LiveData<GetCommentReelResponse> get() = _commentReelResponse

    private val _addCommentReelResponse = MutableLiveData<AddCommentReelResponse>()
    val addCommentReelResponse: LiveData<AddCommentReelResponse> get() = _addCommentReelResponse

    fun addReelComment(
        reelId: String,
        commentRequest: AddCommentReelRequest,
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.addReelComment(reelId, commentRequest)
                if (response.isSuccessful) {
                    _addCommentReelResponse.value = response.body()
                } else {
                    _addCommentReelResponse.value =
                        AddCommentReelResponse(
                            success = false,
                            message = "Thêm bình luận thất bại!",
                            reel = null,
                        )
                }
            } catch (e: Exception) {
                _addCommentReelResponse.value =
                    AddCommentReelResponse(
                        success = false,
                        message = "Có lỗi xảy ra!",
                        reel = null,
                    )
            }
        }
    }

    fun createReel(createReelRequest: CreateReelRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.createReel(createReelRequest)
                _createReelResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Create Post failed", e)
            }
        }
    }

    fun getListReel() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getListReel()
                _reel.value = response
                Log.d("ReelViewModel", response.toString())
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Get list reel failed", e)
            }
        }
    }

    fun likeReel(
        postId: String,
        userId: String,
    ) {
        viewModelScope.launch {
            try {
                val userIdMap = mapOf("userId" to userId)
                val response = ApiClient.apiService.likeReel(postId, userIdMap)
                if (response.isSuccessful) {
                    val updatedPost = response.body()?.reel
                    updatedPost?.let {
                        val currentList = _reel.value?.toMutableList() ?: mutableListOf()
                        val index = currentList.indexOfFirst { it._id == postId }
                        if (index >= 0) {
                            currentList[index] = updatedPost
                            _reel.value = currentList // Dùng setValue để cập nhật _posts
                            _likeReelResponse.value = response.body() // Gửi phản hồi về like
                            Log.d("ReelViewModel", "Like post success: ${response.body()}")
                        }
                    }
                } else {
                    Log.e(
                        "HomeViewModel",
                        "Error liking post: ${response.errorBody()?.string()}",
                    )
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error liking post", e)
            }
        }
    }

    fun getReelComments(reelId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getReelComments(reelId)
                _commentReelResponse.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Get comment failed", e)
            }
        }
    }
}
