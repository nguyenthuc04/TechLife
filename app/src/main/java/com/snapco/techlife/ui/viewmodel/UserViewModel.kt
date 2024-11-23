@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.snapco.techlife.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.api.ApiClient
import com.snapco.techlife.data.model.*
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _followResponse = MutableLiveData<FollowResponse?>()
    val followResponse: LiveData<FollowResponse?> get() = _followResponse

    private val _unfollowResponse = MutableLiveData<UnfollowResponse?>()
    val unfollowResponse: LiveData<UnfollowResponse?> get() = _unfollowResponse

    private val _loginNoHashResponse = MutableLiveData<LoginResponse?>()
    val loginNoHashResponse: LiveData<LoginResponse?> get() = _loginNoHashResponse

    private val _updateUserResponse = MutableLiveData<UpdateUserResponse>()
    val updateUserResponse: LiveData<UpdateUserResponse> get() = _updateUserResponse

    private val _createUserResponse = MutableLiveData<CreateUserResponse>()
    val createUserResponse: LiveData<CreateUserResponse> get() = _createUserResponse

    private val _checkEmailResponse = MutableLiveData<CheckEmailResponse>()
    val checkEmailResponse: LiveData<CheckEmailResponse> get() = _checkEmailResponse

    private val _userResponse = MutableLiveData<GetUserResponse>()
    val userResponse: LiveData<GetUserResponse> get() = _userResponse

    private val _createPremiumResponse = MutableLiveData<CreatePremiumResponse>()
    val createPremiumResponse: LiveData<CreatePremiumResponse> get() = _createPremiumResponse
    private val client: ChatClient by lazy { ChatClient.instance() }

    fun createPremium(premiumRequest: PremiumRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.createPremium(premiumRequest)
                _createPremiumResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("ReelViewModel", "Create Post failed", e)
            }
        }
    }

    fun createUser(createUserRequest: CreateUserRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.createUser(createUserRequest)
                Log.d("DATVMD", "createUser: token = ${response.streamToken}")
                _createUserResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Create user failed", e)
            }
        }
    }

    fun login(
        account: String,
        password: String,
    ) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(account, password)
                val response = ApiClient.apiService.login(loginRequest)
                _loginResponse.value = response
            } catch (e: Exception) {
                _loginResponse.value = null // or set a specific error value
                Log.e("UserViewModel", "Login failed", e)
            }
        }
    }

    fun loginNoHash(
        account: String,
        password: String,
    ) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(account, password)
                val response = ApiClient.apiService.loginNoHash(loginRequest)
                _loginNoHashResponse.value = response
            } catch (e: Exception) {
                _loginNoHashResponse.value = null // or set a specific error value
                Log.e("UserViewModel", "Login failed", e)
            }
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getUser(userId)
                _userResponse.value = response
            } catch (e: Exception) {
                Log.e("UserViewModel", "Get user failed", e)
            }
        }
    }

    fun updateNameUserChat(
        userId: String,
        newName: String,
    ) {
        // Lấy thông tin user hiện tại từ ChatClient
        val currentUser = ChatClient.instance().getCurrentUser()

        if (currentUser != null && currentUser.id == userId) {
            // Tạo một đối tượng User mới với tên cập nhật
            val updatedUser =
                io.getstream.chat.android.models.User(
                    id = currentUser.id,
                    name = newName,
                    image = currentUser.image, // Giữ nguyên ảnh đại diện nếu có
                )

            // Cập nhật thông tin user trên server của GetStream
            client.updateUser(updatedUser).enqueue { result ->
                if (result.isSuccess) {
                    Log.d("UpdateUser", "Cập nhật tên thành công: ${result.getOrNull()!!.name}")
                } else {
                    Log.e(
                        "UpdateUser",
                        "Cập nhật tên thất bại: ${result.errorOrNull()!!.message}",
                    )
                }
            }
        } else {
            println("Người dùng không tồn tại hoặc không trùng khớp ID.")
        }
    }

    fun connectChat(
        userId: String,
        account: String,
        streamToken: String,
    ) {
        client
            .connectUser(
                io.getstream.chat.android.models
                    .User(id = userId, name = account),
                streamToken,
            ).enqueue { result ->
                if (result.isSuccess) {
                    Log.d("checkm", "connectChat: connect ok")
                } else {
                    Log.d("checkm", "connectChat: connect fail ${result.errorOrNull()}")
                }
            }
    }

    fun updateUser(
        userId: String,
        updateUserRequest: UpdateUserRequest,
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.updateUser(userId, updateUserRequest)
                _updateUserResponse.value = response
            } catch (e: Exception) {
                Log.e("UserViewModel", "Update user failed", e)
            }
        }
    }

    fun checkEmail(account: String) {
        viewModelScope.launch {
            try {
                val checkEmailRequest = CheckEmailRequest(account)
                val response = ApiClient.apiService.checkEmail(checkEmailRequest)
                _checkEmailResponse.value = response
            } catch (e: Exception) {
                Log.e("UserViewModel", "Check email failed", e)
            }
        }
    }

    fun getListUsers() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getListUsers()
                _users.value = response
            } catch (e: Exception) {
                Log.e("UserViewModel", "Get list users failed", e)
            }
        }
    }

    fun followUser(followRequest: FollowRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.followUser(followRequest)
                _followResponse.value = response
                Log.d("UserViewModel", "Follow user success: ${response.success}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Follow user failed", e)
            }
        }
    }

    fun unfollowUser(unfollowRequest: UnfollowRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.unfollowUser(unfollowRequest)
                _unfollowResponse.value = response
                Log.d("UserViewModel", "Unfollow user success: ${response.success}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Unfollow user failed", e)
            }
        }
    }
}
