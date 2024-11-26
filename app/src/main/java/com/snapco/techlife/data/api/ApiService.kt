package com.snapco.techlife.data.api

import com.snapco.techlife.data.model.AddCommentReelRequest
import com.snapco.techlife.data.model.AddCommentReelResponse
import com.snapco.techlife.data.model.AddCommentRequest
import com.snapco.techlife.data.model.AddCommentResponse
import com.snapco.techlife.data.model.CheckEmailRequest
import com.snapco.techlife.data.model.CheckEmailResponse
import com.snapco.techlife.data.model.Course
import com.snapco.techlife.data.model.CourseProfileResponse
import com.snapco.techlife.data.model.CourseResponse
import com.snapco.techlife.data.model.CreateCourseRequest
import com.snapco.techlife.data.model.CreatePostRequest
import com.snapco.techlife.data.model.CreatePostResponse
import com.snapco.techlife.data.model.CreatePremiumResponse
import com.snapco.techlife.data.model.CreateReelRequest
import com.snapco.techlife.data.model.CreateReelResponse
import com.snapco.techlife.data.model.CreateUserRequest
import com.snapco.techlife.data.model.CreateUserResponse
import com.snapco.techlife.data.model.FollowRequest
import com.snapco.techlife.data.model.FollowResponse
import com.snapco.techlife.data.model.GetCommentReelResponse
import com.snapco.techlife.data.model.GetCommentResponse
import com.snapco.techlife.data.model.GetUserResponse
import com.snapco.techlife.data.model.LikeReelResponse
import com.snapco.techlife.data.model.LikeResponse
import com.snapco.techlife.data.model.LoginRequest
import com.snapco.techlife.data.model.LoginResponse
import com.snapco.techlife.data.model.Post
import com.snapco.techlife.data.model.PostProfileResponse
import com.snapco.techlife.data.model.PremiumRequest
import com.snapco.techlife.data.model.Reel
import com.snapco.techlife.data.model.ReelProfileResponse
import com.snapco.techlife.data.model.UnfollowRequest
import com.snapco.techlife.data.model.UnfollowResponse
import com.snapco.techlife.data.model.UpdateCourseRequest
import com.snapco.techlife.data.model.UpdateCourseResponse
import com.snapco.techlife.data.model.UpdateUserRequest
import com.snapco.techlife.data.model.UpdateUserResponse
import com.snapco.techlife.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @DELETE("/deleteCourse/{courseId}")
    suspend fun deleteCourse(
        @Path("courseId") courseId: String,
    ): Response<Unit>

    @PUT("/updateCourse/{courseId}")
    suspend fun updateCourse(
        @Path("courseId") courseId: String,
        @Body updateCourseRequest: UpdateCourseRequest,
    ): UpdateCourseResponse

    @GET("/getCoursesByUser/{userId}")
    suspend fun getCoursesByUser(
        @Path("userId") userId: String,
    ): CourseProfileResponse

    @POST("/createCourse")
    suspend fun createCourse(
        @Body createCourseRequest: CreateCourseRequest,
    ): CourseResponse

    @GET("getListCourses")
    suspend fun getCourses(): List<Course>

    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginResponse

    @POST("/login1")
    suspend fun loginNoHash(
        @Body loginRequest: LoginRequest,
    ): LoginResponse

    @POST("/createUser")
    suspend fun createUser(
        @Body createUserRequest: CreateUserRequest,
    ): CreateUserResponse

    @PUT("/updateUser/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body updateUserRequest: UpdateUserRequest,
    ): UpdateUserResponse

    @GET("/getListUsers")
    suspend fun getListUsers(): List<User>

    @GET("/getUser/{id}")
    suspend fun getUser(
        @Path("id") userId: String,
    ): GetUserResponse

    @POST("/checkEmail")
    suspend fun checkEmail(
        @Body checkEmailRequest: CheckEmailRequest,
    ): CheckEmailResponse

    @GET("/getComments/{id}")
    suspend fun getComments(
        @Path("id") userId: String,
    ): GetCommentResponse

    // home
    @POST("/addComment/{postId}")
    suspend fun addComment(
        @Path("postId") postId: String,
        @Body comment: AddCommentRequest,
    ): Response<AddCommentResponse>

    @GET("/getReelComments/{reelId}")
    suspend fun getReelComments(
        @Path("reelId") reelId: String,
    ): GetCommentReelResponse

    @POST("/addReelComment/{reelId}")
    suspend fun addReelComment(
        @Path("reelId") reelId: String,
        @Body comment: AddCommentReelRequest,
    ): Response<AddCommentReelResponse>

    @POST("likePost/{postId}")
    suspend fun likePost(
        @Path("postId") postId: String,
        @Body userId: Map<String, String>,
    ): Response<LikeResponse>

    @POST("likeReel/{reelId}")
    suspend fun likeReel(
        @Path("reelId") postId: String,
        @Body userId: Map<String, String>,
    ): Response<LikeReelResponse>

    @POST("createPost")
    suspend fun createPost(
        @Body createPostRequest: CreatePostRequest,
    ): CreatePostResponse

    @GET("/getListPost")
    suspend fun getListPosts(): List<Post>

    @POST("createReel")
    suspend fun createReel(
        @Body createReelRequest: CreateReelRequest,
    ): CreateReelResponse

    @GET("/getListReel")
    suspend fun getListReel(): List<Reel>

    @GET("/getPostsByUser/{userId}")
    suspend fun getPostsByUser(
        @Path("userId") userId: String,
    ): PostProfileResponse

    @GET("/getReelsByUser/{userId}")
    suspend fun getReelsByUser(
        @Path("userId") userId: String,
    ): ReelProfileResponse

    @POST("/follow")
    suspend fun followUser(
        @Body followRequest: FollowRequest,
    ): FollowResponse

    @POST("/unfollow")
    suspend fun unfollowUser(
        @Body unfollowRequest: UnfollowRequest,
    ): UnfollowResponse

    @POST("createPremium")
    suspend fun createPremium(
        @Body createPremiumRequest: PremiumRequest,
    ): CreatePremiumResponse

    @DELETE("/deletePost/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: String,
    ): Response<Unit>

    @PUT("/updatePost/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: String,
        @Body updateRequest: Map<String, String>
    ): Response<Unit>

    @GET("getCoursesByName/{name}") // Thay endpoint thực tế của bạn vào đây
    suspend fun searchCourses(
        @Path("name") name: String,
    ): CourseResponse
}
