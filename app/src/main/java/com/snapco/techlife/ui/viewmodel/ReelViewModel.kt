package com.snapco.techlife.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.api.ApiClient.apiService
import com.snapco.techlife.data.model.AddCommentReelRequest
import com.snapco.techlife.data.model.AddCommentReelResponse
import com.snapco.techlife.data.model.CreateReelRequest
import com.snapco.techlife.data.model.CreateReelResponse
import com.snapco.techlife.data.model.GetCommentReelResponse
import com.snapco.techlife.data.model.GetReelIdResponse
import com.snapco.techlife.data.model.LikeReelNotificationRequest
import com.snapco.techlife.data.model.LikeReelResponse
import com.snapco.techlife.data.model.Reel
import com.snapco.techlife.extensions.ReelPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    private val _getReelIdResponse = MutableLiveData<GetReelIdResponse>()
    val getReelIdResponse: LiveData<GetReelIdResponse> get() = _getReelIdResponse

    private val _addCommentReelResponse = MutableLiveData<AddCommentReelResponse>()
    val addCommentReelResponse: LiveData<AddCommentReelResponse> get() = _addCommentReelResponse

    fun getReelById(reelId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getReelById(reelId)
                _getReelIdResponse.value = response
                Log.d("ReelViewModel", response.toString())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Get comment failed", e)
            }
        }
    }

    fun getReels(): Flow<PagingData<Reel>> =
        Pager(PagingConfig(pageSize = 3)) {
            ReelPagingSource(apiService)
        }.flow.cachedIn(viewModelScope)

    fun addReelComment(
        reelId: String,
        commentRequest: AddCommentReelRequest,
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.addReelComment(reelId, commentRequest)
                if (response.isSuccessful) {
                    _addCommentReelResponse.value = response.body()
                } else {
                    Log.e("ReelViewModel", "Error adding comment: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Error adding comment", e)
            }
        }
    }

    fun createReel(createReelRequest: CreateReelRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.createReel(createReelRequest)
                _createReelResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Create Post failed", e)
            }
        }
    }

//    fun getListReel() {
//        viewModelScope.launch {
//            try {
//                val response = ApiClient.apiService.getListReel()
//                _reel.value = response
//                Log.d("ReelViewModel", response.toString())
//            } catch (e: Exception) {
//                Log.e("ReelViewModel", "Get list reel failed", e)
//            }
//        }
//    }

    fun likeReel(
        postId: String,
        likeReelNotificationRequest: LikeReelNotificationRequest,
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.likeReel(postId, likeReelNotificationRequest)
                if (response.isSuccessful) {
                    _likeReelResponse.value = response.body()
                    Log.d("ReelViewModel", response.body().toString())
                } else {
                    Log.e("ReelViewModel", "Error liking post: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Error liking post", e)
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
