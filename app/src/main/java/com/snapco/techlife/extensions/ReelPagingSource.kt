package com.snapco.techlife.extensions

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.snapco.techlife.data.api.ApiService
import com.snapco.techlife.data.model.Reel

class ReelPagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, Reel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Reel> =
        try {
            val nextPage = params.key ?: 1
            val response = apiService.getReels(nextPage, params.loadSize)
            LoadResult.Page(
                data = response.reels,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.reels.isEmpty()) null else nextPage + 1,
            )
        } catch (e: Exception) {
            Log.e("ReelPagingSource", "Error loading page", e)
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, Reel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                ?: state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
        }
}
