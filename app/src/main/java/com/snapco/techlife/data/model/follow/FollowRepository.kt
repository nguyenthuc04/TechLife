package com.snapco.techlife.data.model.follow

import com.snapco.techlife.data.model.FollowRequest
import com.snapco.techlife.data.model.FollowResponse
import com.snapco.techlife.data.model.UnfollowRequest
import com.snapco.techlife.data.model.UnfollowResponse

class FollowRepository {
    private val apiService = FollowService.create()

    suspend fun followUser(followRequest: FollowRequest): FollowResponse {
        return apiService.followUser(followRequest)
    }

    suspend fun unfollowUser(unfollowRequest: UnfollowRequest): UnfollowResponse {
        return apiService.unfollowUser(unfollowRequest)
    }
}