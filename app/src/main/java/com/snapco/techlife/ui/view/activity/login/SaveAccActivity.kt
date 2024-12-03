package com.snapco.techlife.ui.view.activity.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.User
import com.snapco.techlife.data.model.UserAccount
import com.snapco.techlife.databinding.ActivitySaveAccBinding
import com.snapco.techlife.extensions.AccountManager
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.activity.MainActivity
import com.snapco.techlife.ui.view.activity.signup.SignUpEmailActivity
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class SaveAccActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveAccBinding
    private val userViewModel: UserViewModel by viewModels()
    private val accountManager by lazy { AccountManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySaveAccBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
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

    private fun setupUI() {
        accountManager.getAccounts().firstOrNull()?.let { currentUser ->
            binding.apply {
                imageView5.setOnClickListener { startActivity<DeleteSaveAccActivity>() }
                imgAvatar.loadImage(currentUser.avatar)
                textView2.text = currentUser.name
                btnLogin.setOnClickListener { handleLogin(currentUser) }
                btnSignInOther.setOnClickListener { startActivity<LoginActivity>() }
                btnSignUp.setOnClickListener { startActivity<SignUpEmailActivity>() }
            }
        }
    }

    private fun handleLogin(currentUser: UserAccount) {
        if (currentUser.state == "false") {
            val intent = Intent(this, LoginRequiresPasswordActivity::class.java)
            intent.putExtra("currentUser", currentUser)
            startActivity(intent)
        } else {
            binding.btnLogin.text = ""
            binding.progressBar.visible()
            userViewModel.loginNoHash(currentUser.account, currentUser.password!!)
        }
    }

    private fun observeLoginResponse() {
        userViewModel.loginNoHashResponse.observe(this) { response ->
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
            accountManager.getAccounts().find { acc -> acc.id == it.id }?.let { savedAccount ->
                val updatedAccount = savedAccount.copy(state = "true", status = "true")
                accountManager.updateAccount(updatedAccount)
                Log.d("AccountManager", "Account updated: $updatedAccount")
            } ?: Log.d("AccountManager", "Account not found")
            userViewModel.connectChat(it.id, it.name, tokenChat, it.avatar)
            userViewModel.updateLastLogin(it.id)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnLogin.text = "Đăng nhập"
                binding.progressBar.gone()
                startActivity<MainActivity>()
            }, 2000)
        }
    }

    private fun showLoginFailedDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Đăng nhập thất bại")
            .setMessage("Tài khoản hoặc mật khẩu của bạn sai")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
