package com.snapco.techlife.ui.viewmodel.messenger

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.querysort.QuerySortByField
import io.getstream.chat.android.ui.feature.channels.list.adapter.ChannelListItem
import kotlinx.coroutines.launch

class ChannelViewModel : ViewModel() {
    private val _channels = MutableLiveData<List<Channel>>()
    val channels: LiveData<List<Channel>> = _channels

    private val _channelExists = MutableLiveData<Boolean>()
    val channelExists: LiveData<Boolean> = _channelExists

    private val _channelsSearch = MutableLiveData<List<ChannelListItem>>()
    val channelsSearch: LiveData<List<ChannelListItem>> get() = _channelsSearch

    private val client = ChatClient.instance()

    fun getListChannel(idUser: String) {
        viewModelScope.launch {
            val request = QueryChannelsRequest(
                filter = Filters.and(
                    Filters.eq("type", "messaging"),
                    Filters.`in`("members", listOf(idUser)),
                ),
                offset = 0,
                limit = 50,
                querySort = QuerySortByField.descByName("lastMessageAt") // Sắp xếp theo tin nhắn mới nhất
            ).apply {
                watch = true
                state = true
            }

            client.queryChannels(request).enqueue { result ->
                if (result.isSuccess) {
                    _channels.postValue(result.getOrNull() ?: emptyList())
                    Log.d("fux123", "getListChannel: day la ${channels.value}")
                } else {
                    // Xử lý lỗi
                    Log.d("fux123", "getListChannel: loi ${result.errorOrNull()}")
                }
            }
        }
    }

    fun checkChannelExists(channelId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val filter = Filters.eq("cid", "messaging:$channelId")
            val request = QueryChannelsRequest(
                filter = filter,
                offset = 0,
                limit = 1 // Chỉ cần kiểm tra 1 kênh
            )

            client.queryChannels(request).enqueue { result ->
                if (result.isSuccess && !result.getOrNull().isNullOrEmpty()) {
                    _channelExists.postValue(true)
                    onResult(true)
                } else {
                    _channelExists.postValue(false)
                    onResult(false)
                }
            }
        }
    }

    fun createChannel(channelId: String,userIdSelect:String, extraData: Map<String, Any>) {
        viewModelScope.launch {
            try {
                client.createChannel(
                    channelType = "messaging",
                    channelId = channelId,
                    memberIds = listOf(client.getCurrentUser()!!.id, userIdSelect),
                    extraData = extraData
                ).enqueue { result ->
                    if (result.isSuccess) {
                        println("tao ok")
                    } else {
                        println("tao fail")
                    }
                }
            }catch (e:Exception){
                Log.d("fixmoi", "getListChannel: loi $e")
            }


        }
    }

}