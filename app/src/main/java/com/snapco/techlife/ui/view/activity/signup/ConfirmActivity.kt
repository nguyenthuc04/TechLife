package com.snapco.techlife.ui.view.activity.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityConfirmBinding
import com.snapco.techlife.extensions.EmailAuthViewModelFactory
import com.snapco.techlife.extensions.showToast
import com.snapco.techlife.ui.viewmodel.EmailAuthViewModel

class ConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBinding

    private val emailAuthViewModel: EmailAuthViewModel by viewModels {
        EmailAuthViewModelFactory(applicationContext)
    }

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
        binding.btnNext.setOnClickListener {
            val code = binding.editConfirm.text.toString()
            if (code.isEmpty()) {
                showToast("Vui lòng nhập mã xác nhận")
            } else {
                if (emailAuthViewModel.verifyOtp(code)) {
                    handleAuthResult(true)
                } else {
                    handleAuthResult(false)
                }
            }
        }
        binding.textView.text =
            "Để xác nhận tài khoản của bạn, hãy nhập mã gồm 6 chữ số mà chứng tôi đã gửi đến địa chỉ email của bạn."

        emailAuthViewModel.error.observe(
            this,
            Observer {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            },
        )
    }

    private fun handleAuthResult(isSuccess: Boolean) {
        if (isSuccess) {
            showToast("Xác minh thành công")
            startActivity(Intent(this, CreatePassActivity::class.java))
            finish()
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
