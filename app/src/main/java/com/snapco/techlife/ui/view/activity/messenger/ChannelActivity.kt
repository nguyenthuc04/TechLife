package com.snapco.techlife.ui.view.activity.messenger

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.snapco.techlife.databinding.ActivityChannelBinding
import com.snapco.techlife.ui.viewmodel.messenger.ChannelViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.querysort.QuerySortByField
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModelFactory
import io.getstream.chat.android.ui.viewmodel.channels.bindView

class ChannelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChannelBinding
    private val channelListHeaderViewModel: ChannelListHeaderViewModel by viewModels()
    private lateinit var listChannelViewModel: ChannelViewModel
    private val client: ChatClient by lazy { ChatClient.instance() }

    private val channelListFactory: ChannelListViewModelFactory = ChannelListViewModelFactory(
        filter = Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf(client.getCurrentUser()!!.id)),
        ),
        sort = QuerySortByField.descByName("last_updated"),
        limit = 30,
    )
    private val channelListViewModel: ChannelListViewModel by viewModels { channelListFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChannelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listChannelViewModel = ViewModelProvider(this)[ChannelViewModel::class]
        // Binding the view models to the UI
        channelListHeaderViewModel.bindView(binding.channelListHeaderView, this)
        channelListViewModel.bindView(binding.channelListView, this)


        // Set up click listeners for the channel list
        binding.channelListView.setChannelItemClickListener { channel ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("ID", channel.id)
            startActivity(intent)
        }

        // Setting the header title
        client.getCurrentUser()?.let {
            binding.channelListHeaderView.setOnlineTitle("Đoạn chat")
        }

        // Set custom view holder factory
        val customFactory = ChannelViewHolder()
        binding.channelListView.setViewHolderFactory(customFactory)

        // Search functionality
//        binding.edtSearch.addTextChangedListener { text ->
//            val query = text.toString()
//            if (query.isNotEmpty()) {
//                listChannelViewModel.searchChannels(query) // Gọi phương thức tìm kiếm
//            }
//        }

        // Handle back press and logout
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            @SuppressLint("CheckResult")
            override fun handleOnBackPressed() {
                logOutUser {
                    val intent = Intent(this@ChannelActivity, LoginMessActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    // Log out user function
    @SuppressLint("CheckResult")
    private fun logOutUser(onLogoutSuccess: () -> Unit) {
        client.disconnect(flushPersistence = true)
        onLogoutSuccess()
    }
}