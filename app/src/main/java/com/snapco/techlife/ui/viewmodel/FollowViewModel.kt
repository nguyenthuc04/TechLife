package com.snapco.techlife.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.model.FollowRequest
import com.snapco.techlife.data.model.FollowResponse
import com.snapco.techlife.data.model.UnfollowRequest
import com.snapco.techlife.data.model.UnfollowResponse
import com.snapco.techlife.data.model.follow.FollowRepository
import kotlinx.coroutines.launch

class FollowViewModel(private val repository: FollowRepository) : ViewModel() {
    val followResponse = MutableLiveData<FollowResponse>()
    val unfollowResponse = MutableLiveData<UnfollowResponse>()

    fun followUser(followRequest: FollowRequest) {
        viewModelScope.launch {
            followResponse.value = repository.followUser(followRequest)
        }
    }

    fun unfollowUser(unfollowRequest: UnfollowRequest) {
        viewModelScope.launch {
            unfollowResponse.value = repository.unfollowUser(unfollowRequest)
        }
    }
}