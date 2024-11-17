package com.snapco.techlife.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.data.model.SearchUserResponse
import com.snapco.techlife.data.search.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()

    private val _users = MutableLiveData<List<SearchUserResponse>>()
    val users: LiveData<List<SearchUserResponse>> get() = _users

    // LiveData để lưu trữ trạng thái tìm kiếm
    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> get() = _isSearching

    private val _user = MutableLiveData<SearchUserResponse>()
    val user: LiveData<SearchUserResponse> get() = _user

    // Phương thức này để tìm kiếm người dùng theo tên
    fun searchUsers(name: String) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                _isSearching.postValue(false) // Cập nhật trạng thái tìm kiếm là false nếu tên rỗng
                _users.postValue(emptyList()) // Cập nhật LiveData với danh sách rỗng
            } else {
                _isSearching.postValue(true) // Cập nhật trạng thái tìm kiếm là true
                val userList = repository.searchUsers(name) // Tìm kiếm người dùng theo tên từ repository
                _users.postValue(userList) // Cập nhật LiveData với danh sách người dùng tìm được
            }
        }
    }

    fun setUser(user: SearchUserResponse) {
        _user.value = user
    }
}
