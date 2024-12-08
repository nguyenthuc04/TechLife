package com.snapco.techlife.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.data.model.NotificationPost
import com.snapco.techlife.databinding.ActivityNotificationBinding
import com.snapco.techlife.ui.view.activity.profile.PostDetailActivity
import com.snapco.techlife.ui.view.adapter.NotificationAdapter
import com.snapco.techlife.ui.viewmodel.NotificationViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class NotificationActivity :
    AppCompatActivity(),
    NotificationAdapter.onNotificationActionListener {
    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
        notificationAdapter = NotificationAdapter(mutableListOf(), this)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = notificationAdapter
        }
    }

    override fun onNotificationClick(notificationPost: NotificationPost) {
        when (notificationPost.contentType) {
            "post" -> navigateToPost(notificationPost.contentId)
            "reel" -> navigateToReel(notificationPost.contentId)
        }
    }

    private fun navigateToPost(postId: String) {
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("postId", postId)
        startActivity(intent)
    }

    private fun navigateToReel(reelId: String) {
        // Implement navigation to the reel screen
    }
}
