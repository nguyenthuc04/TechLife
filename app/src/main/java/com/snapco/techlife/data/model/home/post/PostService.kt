package com.snapco.techlife.data.model.home.post

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PostService {

    // Fetch all posts by a specific user
    @GET("getUserPosts/{userId}")
    fun fetchUserPosts(@Path("userId") userId: String): Call<List<Post>>

    // Fetch a single post by ID
    @GET("getPost/{postId}")
    fun fetchPostById(@Path("postId") postId: String): Call<Post>

    // Create a new post with optional image file
    @Multipart
    @POST("createPost")
    fun createPost(
        @Part("post_data") postData: String, // Serialized JSON post data
        @Part file: MultipartBody.Part? = null // Image file, if provided
    ): Call<Post>

    // Update an existing post by ID, with optional new image file
    @Multipart
    @PUT("updatePost/{postId}")
    fun updatePost(
        @Path("postId") postId: String,
        @Part("post_data") postData: String, // Serialized JSON post data
        @Part file: MultipartBody.Part? = null // Updated image file, if provided
    ): Call<Void>

    // Delete a post by ID
    @DELETE("deletePost/{postId}")
    fun deletePost(@Path("postId") postId: String): Call<Void>
}
