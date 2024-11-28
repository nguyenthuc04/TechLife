package com.snapco.techlife.ui.view.activity.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityForgotPasswordEmailBinding
import com.snapco.techlife.extensions.isEmailValid
import com.snapco.techlife.extensions.showCustomAlertDialog
import com.snapco.techlife.extensions.showToast
import com.snapco.techlife.ui.viewmodel.UserViewModel

class ForgotPasswordEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordEmailBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        setupListeners()
        userViewModel.sendEmailResponse.observe(this) { response ->
            if (response != null) {
                if (response.message == "Reset code sent to email") {
                    val intent = Intent(this, ConfirmAccActivity::class.java)
                    intent.putExtra("email", binding.editEmail.text.toString())
                    intent.putExtra("resetCode", response.code)
                    startActivity(intent)
                } else {
                    binding.btnNext.text = "Tiếp tục"
                    binding.progressBar.visibility = View.GONE
                    showToast("Email không tồn tại")
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnNext.setOnClickListener {
            val email = binding.editEmail.text.toString()
            if (email.isEmpty()) {
                showToast("Vui lòng nhập email")
                return@setOnClickListener
            }
            if (!email.isEmailValid()) {
                showToast("Email không hợp lệ")
                return@setOnClickListener
            }
            binding.btnNext.text = ""
            binding.progressBar.visibility = View.VISIBLE
            userViewModel.sendEmail(email)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar2)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                showCustomAlertDialog(
                    this,
                    title = "Bạn có muốn dừng lấy lại mật khẩu không?",
                    message = "Nếu dừng bây giờ bạn sẽ mất những gì đã thực hiện.",
                    positiveButtonText = "Dừng tạo mật khẩu",
                    negativeButtonText = "Tiếp tục tạo mật khẩu",
                    positiveAction = {
                        finish()
                    },
                    negativeAction = {
                    },
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
}
