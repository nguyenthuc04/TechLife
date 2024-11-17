package com.snapco.techlife.data.search

import com.snapco.techlife.data.model.SearchUserResponse
import com.snapco.techlife.data.repository.SearchRetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository {
    private val apiService: SearchApiService = SearchRetrofitInstance.apiService

    suspend fun searchUsers(name: String): List<SearchUserResponse> =
        withContext(Dispatchers.IO) {
            val response = apiService.searchUsers(name).execute()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        }
}
