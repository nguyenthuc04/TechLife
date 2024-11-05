package com.snapco.techlife.ui.view.activity.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityLoginBinding
import com.snapco.techlife.extensions.getTag
import com.snapco.techlife.extensions.isValidEmail
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.MainActivity
import com.snapco.techlife.ui.view.activity.signup.SignUpEmailActivity
import com.snapco.techlife.ui.viewmodel.UserDataHolder
import com.snapco.techlife.ui.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Suppress("ktlint:standard:property-naming")
    val TAG = getTag()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnSignup.setOnClickListener {
            startActivity<SignUpEmailActivity>()
        }
        userViewModel.loginResponse.observe(
            this,
        ) { response ->
            if (response != null) {
                Log.d(TAG, "Login successful: $response")
                val user = response.user
                if (user != null) {
                    UserDataHolder.setUserData(user.id, user.account, user.name, user.avatar)
                    Log.d(TAG, "User data: ${UserDataHolder.getUserId()}")
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.btnLogin.text = "Đăng nhập"
                    binding.progressBar.visibility = View.GONE
                    startActivity<MainActivity>()
                }, 2000)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.btnLogin.text = "Đăng nhập"
                    binding.progressBar.visibility = View.GONE
                    showLoginFailedDialog()
                }, 100)
            }
        }
    }

    private fun login() {
        val account = binding.editAccount.text.toString()
        val password = binding.editPassword.text.toString()
        if (account.isEmpty()) {
            showError(binding.txtWR, "Vui lòng điền đầy đủ thông tin")
            binding.txtWR2.visibility = View.GONE
            return
        }
        if (password.isEmpty()) {
            showError(binding.txtWR2, "Vui lòng điền đầy đủ thông tin")
            binding.txtWR.visibility = View.GONE
            return
        }
        if (!account.isValidEmail()) {
            binding.txtWR.text = "Email không hợp lệ"
            return
        }
        if (password.length < 8) {
            binding.txtWR2.text = "Mật khẩu phải có ít nhất 8 ký tự"
            return
        }
        binding.txtWR.visibility = View.GONE
        binding.txtWR2.visibility = View.GONE
        Log.d(TAG, "Login with account: $account, password: $password")
        binding.btnLogin.text = ""
        binding.progressBar.visibility = View.VISIBLE
        userViewModel.login(account, password)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar4)
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

    private fun showError(
        textView: TextView,
        message: String,
    ) {
        textView.text = message
        textView.visibility = View.VISIBLE
    }

    private fun showLoginFailedDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Đăng nhập thất bại")
            .setMessage("Tài khoản hoặc mật khẩu của bạn sai")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}
