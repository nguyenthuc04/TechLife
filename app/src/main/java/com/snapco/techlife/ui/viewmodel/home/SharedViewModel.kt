package com.snapco.techlife.ui.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snapco.techlife.data.model.home.Feed

class SharedViewModel : ViewModel() {
    private val _newPost = MutableLiveData<Feed?>()
    val newPost: LiveData<Feed?> = _newPost

    fun setNewPost(post: Feed) {
        _newPost.value = post
    }

    fun clearNewPost() {
        _newPost.value = null
    }
}