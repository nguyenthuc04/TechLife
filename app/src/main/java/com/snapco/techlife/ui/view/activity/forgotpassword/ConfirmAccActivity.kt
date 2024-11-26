package com.snapco.techlife.ui.view.activity.forgotpassword

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityConfirmBinding
import com.snapco.techlife.extensions.showToast
import com.snapco.techlife.ui.viewmodel.UserViewModel

class ConfirmAccActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        val email = intent.getStringExtra("email")
        var resetCode = intent.getStringExtra("resetCode")
        binding.btnNext.setOnClickListener {
            val code = binding.editConfirm.text.toString()
            if (code.isEmpty()) {
                showToast("Vui lòng nhập mã xác nhận")
            } else {
                if (resetCode == code) {
                    handleAuthResult(true, email)
                } else {
                    handleAuthResult(false, email)
                }
            }
        }
        binding.btnNoCode.setOnClickListener {
            userViewModel.sendEmail(email!!)
            val dialog =
                AlertDialog
                    .Builder(this)
                    .setTitle("Gửi lại mã xác nhận")
                    .setMessage("Mã xác nhận mới đã được gửi đến email của bạn")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }.create()
            dialog.show()
        }
        userViewModel.sendEmailResponse.observe(this) { response ->
            if (response != null) {
                if (response.message == "Reset code sent to email") {
                    resetCode = response.code
                } else {
                    showToast("Email không tồn tại")
                }
            }
        }
    }

    private fun handleAuthResult(
        isSuccess: Boolean,
        email: String?,
    ) {
        if (isSuccess) {
            showToast("Xác minh thành công")
            val intent = Intent(this, CreateNewPassActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        } else {
            showToast("Xác minh thất bại")
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
