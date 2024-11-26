package com.snapco.techlife.ui.view.activity.forgotpassword

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
import com.snapco.techlife.data.model.ResetPasswordRequest
import com.snapco.techlife.data.model.User
import com.snapco.techlife.databinding.ActivityCreateNewPassBinding
import com.snapco.techlife.extensions.AccountManager
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.isValidPassword
import com.snapco.techlife.extensions.showToast
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.activity.MainActivity
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class CreateNewPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewPassBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateNewPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        binding.btnNext.setOnClickListener {
            resetPassword()
        }

        observeLoginResponse()
    }

    private fun resetPassword() {
        val email = intent.getStringExtra("email")
        val pass = binding.editPass.text.toString()
        if (pass.isEmpty()) {
            showToast("Vui lòng nhập mật khẩu")
            return
        }
        if (!pass.isValidPassword()) {
            showToast(
                "Có ít nhất một chữ cái viết hoa.\n" +
                    "Có ít nhất một chữ cái viết thường.\n" +
                    "Có ít nhất một chữ số.\n" +
                    "Có ít nhất một ký tự đặc biệt (trong các ký tự @\$!%*?&).\n" +
                    "Độ dài tối thiểu là 6 ký tự.",
            )
            return
        }
        binding.progressBar.visible()
        val resetPass = ResetPasswordRequest(email!!, pass)
        userViewModel.resetPassword(resetPass)
        userViewModel.resetPasswordResponse.observe(this) {
            if (it.success) {
                userViewModel.login(email, pass)
            } else {
                binding.progressBar.gone()
                showToast("Đổi mật khẩu thất bại")
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
        user?.let { it1 ->
            UserDataHolder.setUserData(it1.id, it1.account, it1.name, it1.avatar)
            val accountManager = AccountManager(this)
            accountManager.getAccounts().find { it.id == it1.id }?.let { savedAccount ->
                val updatedAccount = savedAccount.copy(password = binding.editPass.text.toString())
                accountManager.updateAccount(updatedAccount)
            } ?: Log.d("AccountManager", "Account not found")
            userViewModel.updateLastLogin(it1.id)

            userViewModel.connectChat(it1.id, it1.name, tokenChat, it1.avatar)

            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnNext.text = "Tiếp tục"
                binding.progressBar.gone()
                startActivity<MainActivity>()
            }, 2000)
        }
    }

    private fun showLoginFailedDialog() {
        binding.progressBar.gone()
        binding.btnNext.text = "Tiếp tục"
        AlertDialog
            .Builder(this)
            .setTitle("Đăng nhập thất bại")
            .setMessage("Tài khoản hoặc mật khẩu của bạn sai")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar3)
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
