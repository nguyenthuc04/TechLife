package com.snapco.techlife.ui.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.databinding.ActivityNotificationBinding
import com.snapco.techlife.ui.view.adapter.NotificationAdapter
import com.snapco.techlife.ui.viewmodel.NotificationViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        UserDataHolder.getUserId()?.let { viewModel.getNotifications(it) }
        observeNotification()
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun observeNotification() {
        viewModel.notificationResponse.observe(this) { notifications ->
            notifications?.let {
                notificationAdapter.updateNotifications(it.notifications)
            }
        }
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(mutableListOf())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = notificationAdapter
        }
    }
}
