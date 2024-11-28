package com.snapco.techlife.ui.view.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.data.model.Notification
import com.snapco.techlife.databinding.ActivityNotificationBinding
import com.snapco.techlife.ui.view.adapter.NotificationAdapter
import com.snapco.techlife.ui.viewmodel.NotificationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        viewModel.notifications.observe(this) { notifications ->
            if (notifications != null) {
                adapter = NotificationAdapter(notifications) { notification ->
                    onNotificationClick(notification)
                }
                binding.recyclerView.adapter = adapter
            } else {
                // Thêm log để kiểm tra lỗi hoặc hiển thị thông báo cho người dùng
                Log.e("NotificationActivity", "Notifications list is null")
                Toast.makeText(this, "No notifications available", Toast.LENGTH_SHORT).show()
            }
            binding.recyclerView.adapter = adapter
            binding.addNoti.setOnClickListener{
                viewModel.addNotificationAutomatically()
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show()

            }
            binding.backButton.setOnClickListener {
                finish() // Dừng và kết thúc Activity hiện tại
            }
        }


    }


    private fun setupRecyclerView() {
        adapter = NotificationAdapter(mutableListOf()) { notification ->
            onNotificationClick(notification)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = this@NotificationActivity.adapter
        }

    }

    private fun onNotificationClick(notification: Notification) {
        viewModel.markAsSeen(notification._id)
        // Hiển thị thông báo ngắn để xác nhận
        Toast.makeText(this, "Notification marked as seen: ${notification.name}", Toast.LENGTH_SHORT).show()    }
}
