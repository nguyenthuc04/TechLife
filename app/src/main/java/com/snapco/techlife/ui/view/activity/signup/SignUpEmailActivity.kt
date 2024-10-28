package com.snapco.techlife.ui.view.activity.signup

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.User
import com.snapco.techlife.databinding.ActivitySignUpEmailBinding
import com.snapco.techlife.extensions.EmailAuthViewModelFactory
import com.snapco.techlife.extensions.isValidEmail
import com.snapco.techlife.extensions.showCustomAlertDialog
import com.snapco.techlife.extensions.showToast
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.login.LoginActivity
import com.snapco.techlife.ui.viewmodel.EmailAuthViewModel
import com.snapco.techlife.ui.viewmodel.SignUpDataHolder

class SignUpEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpEmailBinding
    private val viewModel: EmailAuthViewModel by viewModels {
        EmailAuthViewModelFactory(applicationContext)
    }
    private val signUpDataHolder: SignUpDataHolder by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        setupListeners()
        binding.btnAccount.setOnClickListener {
            showCustomAlertDialog(
                this,
                title = "Bạn đã có tài khoản ư?",
                message = "",
                positiveButtonText = "Đăng nhập",
                negativeButtonText = "Tiếp tục tạo tài khoản",
                positiveAction = {
                    startActivity<LoginActivity>()
                },
                negativeAction = {
                },
            )
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

    private fun setupListeners() {
        binding.btnAccount.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            val email = binding.editEmail.text.toString()
            if (email.isEmpty()) {
                showToast("Vui lòng nhập email")
                return@setOnClickListener
            }
            if (!email.isValidEmail()) {
                showToast("Email không hợp lệ")
                return@setOnClickListener
            }
            sendVerificationEmail(email)
        }
    }

    private fun sendVerificationEmail(email: String) {
        viewModel.sendVerificationEmail(email)
        viewModel.authResult.observe(this) { success ->
            if (success) {
                val user = User(account = email)
                signUpDataHolder.setUser(user)
                showToast("Gửi email xác minh thành công")
                startActivity<ConfirmActivity>()
            } else {
                showToast("Gửi email xác minh thất bại")
            }
        }
        viewModel.error.observe(this) { error ->
            showToast(error)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                showCustomAlertDialog(
                    this,
                    title = "Bạn có muốn dừng tạo tài khoản không?",
                    message = "Nếu dừng bây giờ bạn sẽ mất những gì đã thực hiện.",
                    positiveButtonText = "Dừng tạo tài khoản",
                    negativeButtonText = "Tiếp tục tạo tài khoản",
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
