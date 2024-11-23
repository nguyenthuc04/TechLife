package com.snapco.techlife.ui.viewmodel.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.model.AddCommentRequest
import com.snapco.techlife.data.model.AddCommentResponse
import com.snapco.techlife.data.model.CreatePostRequest
import com.snapco.techlife.data.model.CreatePostResponse
import com.snapco.techlife.data.model.GetCommentResponse
import com.snapco.techlife.data.model.LikeResponse
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.data.model.PostProfileResponse
import com.snapco.techlife.data.model.ReelProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _postList = MutableLiveData<MutableList<Post>?>()
    val postList: MutableLiveData<MutableList<Post>?> get() = _postList

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    private val _createPostResponse = MutableLiveData<CreatePostResponse>()
    val createPostResponse: LiveData<CreatePostResponse> get() = _createPostResponse

    private val _likeResponse = MutableLiveData<LikeResponse>()
    val likeResponse: LiveData<LikeResponse> get() = _likeResponse

    private val _addCommentResponse = MutableLiveData<AddCommentResponse>()
    val addCommentResponse: LiveData<AddCommentResponse> get() = _addCommentResponse

    private val _commentResponse = MutableLiveData<GetCommentResponse>()
    val commentResponse: LiveData<GetCommentResponse> get() = _commentResponse

    private val _postListProfile = MutableLiveData<PostProfileResponse>()
    val postListProfile: LiveData<PostProfileResponse> get() = _postListProfile

    private val _reelListProfile = MutableLiveData<ReelProfileResponse>()
    val reelListProfile: LiveData<ReelProfileResponse> get() = _reelListProfile

    fun getPostsByUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getPostsByUser(userId)
                _postListProfile.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Get posts by user failed", e)
            }
        }
    }

    fun getReelsByUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getReelsByUser(userId)
                _reelListProfile.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Get posts by user failed", e)
            }
        }
    }

    fun addComment(
        postId: String,
        commentRequest: AddCommentRequest,
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.addComment(postId, commentRequest)
                if (response.isSuccessful) {
                    _addCommentResponse.value = response.body()
                } else {
                    _addCommentResponse.value =
                        AddCommentResponse(
                            success = false,
                            message = "Thêm bình luận thất bại!",
                            post = null,
                        )
                }
            } catch (e: Exception) {
                _addCommentResponse.value =
                    AddCommentResponse(
                        success = false,
                        message = "Có lỗi xảy ra!",
                        post = null,
                    )
            }
        }
    }

    fun getComments(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getComments(userId)
                _commentResponse.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Get comment failed", e)
            }
        }
    }

    fun likePost(
        postId: String,
        userId: String,
    ) {
        viewModelScope.launch {
            try {
                val userIdMap = mapOf("userId" to userId)
                val response = ApiClient.apiService.likePost(postId, userIdMap)
                if (response.isSuccessful) {
                    val updatedPost = response.body()?.post
                    updatedPost?.let {
                        val currentList = _posts.value?.toMutableList() ?: mutableListOf()
                        val index = currentList.indexOfFirst { it._id == postId }
                        if (index >= 0) {
                            currentList[index] = updatedPost
                            _posts.value = currentList // Dùng setValue để cập nhật _posts
                            _likeResponse.value = response.body() // Gửi phản hồi về like
                            Log.d("HomeViewModel", "Like post success: ${response.body()}")
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

    fun createPost(createPostRequest: CreatePostRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.createPost(createPostRequest)
                _createPostResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Create Post failed", e)
            }
        }
    }

    fun getListPosts() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getListPosts()
                _posts.value = response
                Log.d("HomeViewModel", response.toString())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Get list Post failed", e)
            }
        }
    }
}
