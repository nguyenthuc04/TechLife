package com.snapco.techlife.ui.viewmodel.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

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

    private val _randomPosts = MutableLiveData<List<Post>>()
    val randomPosts: LiveData<List<Post>> get() = _randomPosts

    private val _deletePostResponse = MutableLiveData<Boolean>()
    val deletePostResponse: LiveData<Boolean> get() = _deletePostResponse

    private val _updatePostResponse = MutableLiveData<Response<UpdatePostResponse>>()
    val updatePostResponse: LiveData<Response<UpdatePostResponse>> get() = _updatePostResponse

    private val _updateResult = MutableLiveData<Result<Boolean>>()
    val updateResult: LiveData<Result<Boolean>> get() = _updateResult


    fun getPostsByUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getPostsByUser(userId)
                _postListProfile.value = response
                Log.d("HomeViewModel1", response.toString())
            } catch (e: Exception) {
                _postListProfile.value = null
            }
        }
    }

    fun getReelsByUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getReelsByUser(userId)
                _reelListProfile.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Get reels by user failed", e)
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
                            message = "Failed to add comment!",
                            post = null,
                        )
                }
            } catch (e: Exception) {
                _addCommentResponse.value =
                    AddCommentResponse(
                        success = false,
                        message = "An error occurred!",
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
        likeRequest: LikeNotificationRequest,
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.likePost(postId, likeRequest)
                if (response.isSuccessful) {
                    val updatedPost = response.body()?.post
                    updatedPost?.let {
                        val currentList = _posts.value?.toMutableList() ?: mutableListOf()
                        val index = currentList.indexOfFirst { it._id == postId }
                        if (index >= 0) {
                            currentList[index] = updatedPost
                            _posts.value = currentList
                            _likeResponse.value = response.body()
                            Log.d("HomeViewModel", "Like post success: ${response.body()}")
                        }
                    }
                } else {
                    Log.e("HomeViewModel", "Error liking post: ${response.errorBody()?.string()}")
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

    fun deletePost(postId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.deletePost(postId)
                if (response.isSuccessful) {
                    // Remove deleted post from the list
                    val currentList = _posts.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it._id == postId }
                    if (index >= 0) {
                        currentList.removeAt(index)
                        _posts.value = currentList
                        _deletePostResponse.value = true
                        Log.d("HomeViewModel", "Post deleted successfully: $postId")
                    }
                } else {
                    _deletePostResponse.value = false
                    Log.e("HomeViewModel", "Error deleting post: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _deletePostResponse.value = false
                Log.e("HomeViewModel", "Error deleting post", e)
            }
        }
    }

    fun updatePost(
        postId: String,
        updatedCaption: String,
    ) {
        viewModelScope.launch {
            try {
                val updateRequest = mapOf("caption" to updatedCaption)
                val response = ApiClient.apiService.updatePost(postId, updateRequest)
                if (response.isSuccessful) {
                    _updateResult.postValue(Result.success(true))
                } else {
                    _updateResult.postValue(Result.failure(Exception("Failed to update post")))
                }
            } catch (e: Exception) {
                _updateResult.postValue(Result.failure(e))
                Log.e("HomeViewModel", "Error updating post", e)
            }
        }
    }
}
