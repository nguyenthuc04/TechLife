package com.snapco.techlife.data.model.home.post

import com.snapco.techlife.data.model.home.comment.Comment
import com.snapco.techlife.data.model.home.like.Like
import retrofit2.Call
import retrofit2.http.*

interface PostService {

    @GET("getUserPosts/{userId}")
    fun fetchUserPosts(@Path("userId") userId: String): Call<List<Post>>

    @GET("getPost/{postId}")
    fun fetchPostById(@Path("postId") postId: String): Call<Post>

    @POST("createPost")
    fun createPost(
        @Body postData: String // JSON string containing post data including `imageUrl`
    ): Call<Post>

    @PUT("updatePost/{postId}")
    fun updatePost(
        @Path("postId") postId: String,
        @Body postData: String // JSON string containing updated post data including `imageUrl`
    ): Call<Void>

    @DELETE("deletePost/{postId}")
    fun deletePost(@Path("postId") postId: String): Call<Void>

    @POST("posts/{postId}/like")
    fun likePost(@Path("postId") postId: String, @Body userId: String): Call<Like>

    @DELETE("posts/{postId}/like")
    fun unlikePost(@Path("postId") postId: String, @Query("userId") userId: String): Call<Void>

    @POST("posts/{postId}/comment")
    fun addComment(@Path("postId") postId: String, @Body comment: Comment): Call<Comment>

    @GET("posts/{postId}/comments")
    fun getComments(@Path("postId") postId: String): Call<List<Comment>>
}
