package com.snapco.techlife.ui.viewmodel.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class SharedViewModel : ViewModel() {

    private val _postList = MutableLiveData<MutableList<Post>>()
    val postList: LiveData<MutableList<Post>> get() = _postList

    // Fetch all posts for a specific user
    fun fetchPosts(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PostRetrofit.apiService.fetchUserPosts(userId).execute()
                if (response.isSuccessful) {
                    _postList.postValue(response.body()?.toMutableList())
                } else {
                    Log.e("SharedViewModel", "Failed to fetch posts: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error fetching posts", e)
            }
        }
    }

    // Add a new post, optionally with an image file
    fun createPost(post: Post, imageFile: File? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postData = Json.encodeToString(post).toRequestBody()
                val filePart = imageFile?.let {
                    MultipartBody.Part.createFormData("file", it.name, it.asRequestBody())
                }
                val response = PostRetrofit.apiService.createPost(postData.toString(), filePart).execute()

                if (response.isSuccessful) {
                    response.body()?.let {
                        _postList.value?.add(0, it)
                        _postList.postValue(_postList.value)
                    }
                } else {
                    Log.e("SharedViewModel", "Failed to create post: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error creating post", e)
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

    // Delete a post by ID
    fun deletePost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PostRetrofit.apiService.deletePost(postId).execute()
                if (response.isSuccessful) {
                    _postList.value?.removeIf { it.postId == postId }
                    _postList.postValue(_postList.value)
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error deleting post", e)
            }
        }
    }
}