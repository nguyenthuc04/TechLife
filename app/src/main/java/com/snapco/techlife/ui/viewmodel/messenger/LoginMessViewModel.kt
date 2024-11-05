package com.snapco.techlife.ui.viewmodel.messenger

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.ui.viewmodel.messenger.retrofit.RetrofitInstance
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.User
import io.getstream.chat.android.models.querysort.QuerySortByField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Response

class LoginMessViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    private val client = ChatClient.instance()

    fun login(username: String, onResult: (String?) -> Unit) {
        val request = LoginRequest(username)
        val call = RetrofitInstance.api.login(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val streamToken = loginResponse?.streamToken
                    onResult(streamToken)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }


    fun getAllUsers(currentUserId: String) {
        viewModelScope.launch {
            try {
                // Tạo yêu cầu để lấy tất cả người dùng, ngoại trừ người dùng hiện tại
                val request = QueryUsersRequest(
                    filter = Filters.and(
                        Filters.exists("id"),
                        Filters.ne("id", currentUserId) // Loại trừ người dùng hiện tại
                    ),
                    offset = 0,
                    limit = 50,
                    querySort = QuerySortByField.ascByName("id") // Sắp xếp theo ID người dùng
                )

                // Gọi API để truy vấn người dùng
                val result = withContext(Dispatchers.IO) {
                    client.queryUsers(request).await() // Sử dụng await() nếu bạn đang sử dụng coroutines
                }

                if (result.isSuccess) {
                    _users.postValue(result.getOrNull()) // Cập nhật LiveData
                    Log.d("fux123", "getAllUsers: ok ${result.getOrNull()}")
                } else {
                    // Xử lý lỗi result.error()
                    Log.d("fux123", "getAllUsers: else, error: ${result.errorOrNull()}")
                }
            } catch (e: Exception) {
                // Xử lý ngoại lệ
                Log.d("fux123", "getAllUsers: loi $e")
            }
        }
    }
}

data class LoginRequest(val username: String)

data class LoginResponse(
    val jwtToken: String,
    val streamToken: String,
    val apiKey: String
)