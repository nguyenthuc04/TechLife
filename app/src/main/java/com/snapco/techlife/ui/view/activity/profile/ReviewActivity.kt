package com.snapco.techlife.ui.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.data.model.CreateReviewRequest
import com.snapco.techlife.databinding.ActivityReviewBinding
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.ui.view.adapter.ReviewAdapter
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        val userId = intent.getStringExtra("USER_ID")
        setupRecyclerView()
        userViewModel.getReview(userId.toString())
        userViewModel.checkUserInAnyCourse(UserDataHolder.getUserId().toString(), userId.toString())
        userViewModel.checkUserInAnyCourseResponse.observe(this) { checkUserInAnyCourseResponse ->
            checkUserInAnyCourseResponse?.let {
                if (!it.isCheck) {
                    binding.submitButton.gone()
                    binding.commentEditText.gone()
                    binding.starRatingView.gone()
                }
            }
        }
        observeReview()
        binding.submitButton.setOnClickListener {
            val createReviewRequest =
                CreateReviewRequest(
                    idMentor = userId.toString(),
                    userId = UserDataHolder.getUserId().toString(),
                    rating = binding.starRatingView.rating,
                    comment = binding.commentEditText.text.toString(),
                )
            userViewModel.createReview(createReviewRequest)
        }
        observeCreateReview()
    }

    private fun observeCreateReview() {
        userViewModel.createReviewResponse.observe(this) { createReviewResponse ->
            createReviewResponse?.let {
                if (it.success) {
                    binding.commentEditText.text.clear()
                    userViewModel.getReview(intent.getStringExtra("USER_ID").toString())
                }
            }
        }
    }

    private fun observeReview() {
        userViewModel.getReviewResponse.observe(this) { reviewResponse ->
            reviewResponse?.let {
                reviewAdapter.updateReviews(it.reviews)
                val currentUserId = UserDataHolder.getUserId().toString()
                val hasReviewed = it.reviews.any { review -> review.userId._id == currentUserId }
                if (hasReviewed) {
                    binding.submitButton.gone()
                    binding.commentEditText.gone()
                    binding.starRatingView.gone()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter(mutableListOf())
        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewAdapter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar4)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(!intent.getBooleanExtra("hideBackButton", false))
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }
}
