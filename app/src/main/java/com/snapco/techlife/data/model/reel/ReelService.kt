package com.snapco.techlife.data.model.reel

import com.snapco.techlife.data.model.home.comment.Comment
import com.snapco.techlife.data.model.home.like.Like
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ReelService {
    // Fetch all reels by a specific user
    @GET("getUserReels/{userId}")
    fun fetchUserReels(@Path("userId") userId: String): Call<List<Reel>>

    // Fetch a single reel by ID
    @GET("getReel/{reelId}")
    fun fetchReelById(@Path("reelId") reelId: String): Call<Reel>

    // Create a new reel with optional video file
    @Multipart
    @POST("createReel")
    fun createReel(
        @Part("reel_data") reelData: String, // Serialized JSON reel data
        @Part file: MultipartBody.Part? = null // Video file, if provided
    ): Call<Reel>

    // Update an existing reel by ID, with optional new video file
    @Multipart
    @PUT("updateReel/{reelId}")
    fun updateReel(
        @Path("reelId") reelId: String,
        @Part("reel_data") reelData: String, // Serialized JSON reel data
        @Part file: MultipartBody.Part? = null // Updated video file, if provided
    ): Call<Void>

    // Delete a reel by ID
    @DELETE("deleteReel/{reelId}")
    fun deleteReel(@Path("reelId") reelId: String): Call<Void>

    @POST("reels/{reelId}/like")
    fun likeReel(@Path("reelId") reelId: String, @Body userId: String): Call<Like>

    @DELETE("reels/{reelId}/like")
    fun unlikeReel(@Path("reelId") reelId: String, @Query("userId") userId: String): Call<Void>

    @POST("reels/{reelId}/comment")
    fun addCommentReel(@Path("reelId") reelId: String, @Body comment: CommentReel): Call<Comment>

    @GET("reels/{reelId}/comments")
    fun getCommentsReel(@Path("reelId") reelId: String): Call<List<CommentReel>>
}