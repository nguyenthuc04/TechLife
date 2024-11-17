package com.snapco.techlife.data.model.api

import com.snapco.techlife.data.model.CheckEmailRequest
import com.snapco.techlife.data.model.CheckEmailResponse
import com.snapco.techlife.data.model.CreateUserRequest
import com.snapco.techlife.data.model.CreateUserResponse
import com.snapco.techlife.data.model.GetUserResponse
import com.snapco.techlife.data.model.LoginRequest
import com.snapco.techlife.data.model.LoginResponse
import com.snapco.techlife.data.model.UpdateUserRequest
import com.snapco.techlife.data.model.UpdateUserResponse
import com.snapco.techlife.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
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
}
