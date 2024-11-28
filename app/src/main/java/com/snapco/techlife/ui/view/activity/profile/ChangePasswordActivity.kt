package com.snapco.techlife.ui.view.activity.profile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.ChangepasswordRequest
import com.snapco.techlife.databinding.ActivityChangePasswordBinding
import com.snapco.techlife.extensions.AccountManager
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val userViewModel: UserViewModel by viewModels()
    private var newPass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupToolbar()
        setupUI()
        observeChangePasswordResponse()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

    private fun setupUI() {
        binding.btnLogin.setOnClickListener { changePassword() }
    }

    private fun changePassword() {
        val oldPassword = binding.editPassword.text.toString()
        val newPassword = binding.editPassword1.text.toString()
        val confirmPassword = binding.editPassword2.text.toString()

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show()
            return
        }

        val userAcc = UserDataHolder.getUserAccount() ?: return
        val request = ChangepasswordRequest(userAcc, oldPassword, newPassword)
        newPass = newPassword
        binding.progressBar.visible()
        userViewModel.changepassword(request)
    }

    private fun observeChangePasswordResponse() {
        userViewModel.changepasswordResponse.observe(this) { response ->
            response?.let { it1 ->
                binding.progressBar.gone()
                if (it1.success) {
                    UserDataHolder.getUserId()?.let { userId ->
                        GetUserResponseHolder.getGetUserResponse()?.let {
                            val accountManager = AccountManager(this)
                            accountManager.getAccounts().find { it.id == userId }?.let { savedAccount ->
                                val updatedAccount = savedAccount.copy(password = newPass)
                                accountManager.updateAccount(updatedAccount)
                                Log.d("AccountManager", "Account updated: $updatedAccount")
                            } ?: Log.d("AccountManager", "Account not found")
                        }
                    }
                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    showLoginFailedDialog()
                    binding.progressBar.gone()
                }
            }
        }
    }

    private fun showLoginFailedDialog() {
        binding.progressBar.gone()
        AlertDialog
            .Builder(this)
            .setTitle("Đổi mật khẩu thất bại")
            .setMessage(" Mật khẩu cũ của bạn sai")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
