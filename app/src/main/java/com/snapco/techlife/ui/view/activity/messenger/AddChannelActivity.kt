package com.snapco.techlife.ui.view.activity.messenger

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityAddChannelBinding
import com.snapco.techlife.ui.viewmodel.messenger.ChannelViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.querysort.QuerySortByField
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModelFactory
import io.getstream.chat.android.ui.viewmodel.channels.bindView

class AddChannelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddChannelBinding
    private lateinit var channelViewModel: ChannelViewModel
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
        binding = ActivityAddChannelBinding .inflate(layoutInflater)
        setContentView(binding.root)

        channelViewModel = ViewModelProvider(this)[ChannelViewModel::class]
        // Binding the view models to the UI
        channelListViewModel.bindView(binding.listViewAddChannel, this)


        // Set up click listeners for the channel list
        binding.listViewAddChannel.setChannelItemClickListener { channel ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("ID", channel.id)
            startActivity(intent)
        }
        // su kien nut back
        binding.btnBack.setOnClickListener{
            val intent = Intent(this@AddChannelActivity, ChannelActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set custom view holder factory
        val customFactory = AddChannelViewHolder()
        binding.listViewAddChannel.setViewHolderFactory(customFactory)

        setupSearchListener()

// Handle back press and logout
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            @SuppressLint("CheckResult")
            override fun handleOnBackPressed() {
                val intent = Intent(this@AddChannelActivity, ChannelActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun setupSearchListener() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim() // Lấy chuỗi tìm kiếm và loại bỏ khoảng trắng
                val currentUser = client.getCurrentUser()
                val userHT = currentUser?.name // Lấy tên người dùng hiện tại

                if (query.isNotEmpty() && userHT != null) {
                    val filter = Filters.and(
                        Filters.eq("type", "messaging"), // Lọc theo loại kênh
                        Filters.`in`("members", listOf(currentUser.id)), // Lọc theo thành viên
                        Filters.or( // Lọc theo tên người dùng trong extraData
                            Filters.autocomplete("user1_name", query),
                            Filters.autocomplete("user2_name", query)
                        )
                    )

                    channelListViewModel.setFilters(filter)
                } else {
                    // Nếu không có truy vấn tìm kiếm, lấy lại tất cả các kênh
                    channelListViewModel.setFilters(
                        Filters.and(
                            Filters.eq("type", "messaging"),
                            Filters.`in`("members", listOf(currentUser!!.id))
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

}