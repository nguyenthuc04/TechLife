@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.snapco.techlife.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.model.*
import com.snapco.techlife.data.model.api.ApiClient
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

    private val client: ChatClient by lazy { ChatClient.instance() }

    fun createUser(createUserRequest: CreateUserRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.createUser(createUserRequest)
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
}
