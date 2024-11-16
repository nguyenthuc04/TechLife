package com.snapco.techlife.ui.viewmodel.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.model.home.comment.Comment
import com.snapco.techlife.data.model.home.post.Post
import com.snapco.techlife.data.model.home.post.PostRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class HomeViewModel : ViewModel() {

    private val _postList = MutableLiveData<MutableList<Post>?>()
    val postList: MutableLiveData<MutableList<Post>?> get() = _postList

    fun fetchPosts(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PostRetrofit.apiService.fetchUserPosts(userId).execute()
                if (response.isSuccessful) {
                    _postList.postValue(response.body()?.toMutableList())
                } else {
                    Log.e("HomeViewModel", "Failed to fetch posts: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching posts", e)
            }
        }
    }

    // Add a new post, optionally with an image file
    fun createPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postData = Json.encodeToString(post).toRequestBody()
                val response = PostRetrofit.apiService.createPost(postData.toString()).execute()

                if (response.isSuccessful) {
                    response.body()?.let {
                        _postList.value?.add(0, it)
                        _postList.postValue(_postList.value)
                    }
                } else {
                    Log.e("HomeViewModel", "Failed to create post: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error creating post", e)
            }
        }
    }

    // Update an existing post
    fun updatePost(updatedPost: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentList = _postList.value ?: return@launch
            val index = currentList.indexOfFirst { it.postId == updatedPost.postId }
            if (index != -1) {
                currentList[index] = updatedPost
                _postList.postValue(currentList)
            }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PostRetrofit.apiService.deletePost(postId).execute()
                if (response.isSuccessful) {
                    _postList.value?.removeIf { it.postId == postId }
                    _postList.postValue(_postList.value)
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error deleting post", e)
            }
        }
    }

    fun likePost(postId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PostRetrofit.apiService.likePost(postId, userId).execute()
                if (response.isSuccessful) {
                    val currentList = _postList.value
                    val postIndex = currentList?.indexOfFirst { it.postId == postId }
                    postIndex?.let {
                        currentList[it].likesCount += 1
                        _postList.postValue(currentList)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error liking post", e)
            }
        }
    }

    fun unlikePost(postId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PostRetrofit.apiService.unlikePost(postId, userId).execute()
                if (response.isSuccessful) {
                    val currentList = _postList.value
                    val postIndex = currentList?.indexOfFirst { it.postId == postId }
                    postIndex?.let {
                        currentList[it].likesCount -= 1
                        _postList.postValue(currentList)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error unliking post", e)
            }
        }
    }

    fun addComment(postId: String, comment: Comment) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PostRetrofit.apiService.addComment(postId, comment).execute()
                if (response.isSuccessful) {
                    // Add new comment to the post
                    val currentList = _postList.value
                    val postIndex = currentList?.indexOfFirst { it.postId == postId }
                    postIndex?.let {
                        currentList[it].commentsCount += 1
                        currentList[it].comments = currentList[it].comments + comment
                        _postList.postValue(currentList)
                    }
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error adding comment", e)
            }
        }
    }

    fun fetchComments(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PostRetrofit.apiService.getComments(postId).execute()
                if (response.isSuccessful) {
                    val currentList = _postList.value
                    val postIndex = currentList?.indexOfFirst { it.postId == postId }
                    postIndex?.let {
                        currentList[it].comments = response.body() ?: listOf()
                        _postList.postValue(currentList)
                    }
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error fetching comments", e)
            }
        }
    }
}
