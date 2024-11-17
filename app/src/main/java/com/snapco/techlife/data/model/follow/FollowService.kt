// FollowService.kt
package com.snapco.techlife.data.model.follow

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import com.snapco.techlife.data.model.FollowRequest
import com.snapco.techlife.data.model.FollowResponse
import com.snapco.techlife.data.model.UnfollowRequest
import com.snapco.techlife.data.model.UnfollowResponse

interface FollowService {
    @POST("/follow")
    suspend fun followUser(@Body followRequest: FollowRequest): FollowResponse

    @POST("/unfollow")
    suspend fun unfollowUser(@Body unfollowRequest: UnfollowRequest): UnfollowResponse

    companion object {
        fun create(): FollowService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.106:3000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(FollowService::class.java)
        }
    }
}