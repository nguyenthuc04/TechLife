package com.snapco.techlife.ui.view.activity.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.User
import com.snapco.techlife.data.model.UserAccount
import com.snapco.techlife.databinding.ActivityLoginBinding
import com.snapco.techlife.extensions.*
import com.snapco.techlife.ui.view.activity.MainActivity
import com.snapco.techlife.ui.view.activity.forgotpassword.ForgotPasswordEmailActivity
import com.snapco.techlife.ui.view.activity.signup.SignUpEmailActivity
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

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
        setupWindowInsets()
        setupToolbar()
        setupUI()
        observeLoginResponse()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar4)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(!intent.getBooleanExtra("hideBackButton", false))
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }

    private fun setupUI() {
        binding.apply {
            btnLogin.setOnClickListener { login() }
            btnSignup.setOnClickListener { startActivity<SignUpEmailActivity>() }
            btnForgotPass.setOnClickListener { startActivity<ForgotPasswordEmailActivity>() }
        }
    }

    private fun login() {
        val account = binding.editAccount.text.toString()
        val password = binding.editPassword.text.toString()

        when {
            account.isEmpty() -> showError(binding.txtWR, "Vui lòng điền đầy đủ thông tin")
            password.isEmpty() -> showError(binding.txtWR2, "Vui lòng điền đầy đủ thông tin")
            !account.isEmailValid() -> showError(binding.txtWR, "Email không hợp lệ")
            password.length < 8 -> showError(binding.txtWR2, "Mật khẩu phải có ít nhất 8 ký tự")
            else -> {
                binding.txtWR.gone()
                binding.txtWR2.gone()
                Log.d(TAG, "Login with account: $account, password: $password")
                binding.btnLogin.text = ""
                binding.progressBar.visible()
                userViewModel.login(account, password)
            }
        }
    }

    private fun observeLoginResponse() {
        userViewModel.loginResponse.observe(this) { response ->
            response?.let {
                handleLoginResponse(it.user, it.streamToken.toString())
            } ?: run {
                showLoginFailedDialog()
            }
        }
    }

    private fun handleLoginResponse(
        user: User?,
        tokenChat: String,
    ) {
        user?.let {
            UserDataHolder.setUserData(it.id, it.account, it.name, it.avatar)
            val accountManager = AccountManager(this)
            val userAccount =
                UserAccount(
                    id = it.id,
                    account = it.account,
                    password = it.password,
                    avatar = it.avatar,
                    name = it.name,
                    state = "false",
                    status = "true",
                )
            userViewModel.updateLastLogin(it.id)
            accountManager.addAccount(userAccount)
            userViewModel.connectChat(it.id, it.name, tokenChat, it.avatar)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnLogin.text = "Đăng nhập"
                binding.progressBar.gone()
                startActivity<MainActivity>()
            }, 2000)
        }
    }

    private fun showError(
        textView: TextView,
        message: String,
    ) {
        textView.text = message
        textView.visible()
    }

    private fun showLoginFailedDialog() {
        binding.progressBar.gone()
        binding.btnLogin.text = "Đăng nhập"
        AlertDialog
            .Builder(this)
            .setTitle("Đăng nhập thất bại")
            .setMessage("Tài khoản hoặc mật khẩu của bạn sai")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
