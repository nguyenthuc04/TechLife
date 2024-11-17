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
import com.snapco.techlife.databinding.ActivityLoginRequiresPasswordBinding
import com.snapco.techlife.extensions.AccountManager
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.activity.MainActivity
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class LoginRequiresPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRequiresPasswordBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginRequiresPasswordBinding.inflate(layoutInflater)
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
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }

    private fun setupUI() {
        intent.getParcelableExtra<UserAccount>("currentUser")?.let { currentUser ->
            binding.apply {
                textView2.text = currentUser.name
                imgAvatar.loadImage(currentUser.avatar)
                btnLogin.setOnClickListener { login(currentUser.account) }
            }
        }
    }

    private fun login(account: String) =
        binding.apply {
            val password = editPassword.text.toString()

            when {
                password.isEmpty() -> showError(txtWR2, "Vui lòng điền đầy đủ thông tin")
                password.length < 8 -> showError(txtWR2, "Mật khẩu phải có ít nhất 8 ký tự")
                else -> {
                    txtWR2.gone()
                    btnLogin.text = ""
                    progressBar.visible()
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
            updateAccount(it)
            userViewModel.connectChat(it.id, it.account, tokenChat,it.avatar)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnLogin.text = "Đăng nhập"
                binding.progressBar.gone()
                startActivity<MainActivity>()
            }, 2000)
        }
    }

    private fun updateAccount(user: User) {
        val accountManager = AccountManager(this)
        accountManager.getAccounts().find { it.id == user.id }?.let { savedAccount ->
            val updatedAccount = savedAccount.copy(state = "false", status = "true")
            accountManager.updateAccount(updatedAccount)
            Log.d("AccountManager", "Account updated: $updatedAccount")
        } ?: Log.d("AccountManager", "Account not found")
    }

    private fun showError(
        textView: TextView,
        message: String,
    ) {
        textView.text = message
        textView.visible()
    }

    private fun showLoginFailedDialog() {
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
