package com.snapco.techlife.data.repository

import com.snapco.techlife.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository {
    private val apiService: SearchApiService = SearchRetrofitInstance.apiService

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        val response = apiService.getUsers().execute()
        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun searchUsers(name: String): List<User> = withContext(Dispatchers.IO) {
        val response = apiService.searchUsers(name).execute()
        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
}