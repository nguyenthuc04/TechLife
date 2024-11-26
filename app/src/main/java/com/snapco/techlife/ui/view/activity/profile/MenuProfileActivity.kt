package com.snapco.techlife.ui.view.activity.profile

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityMenuProfileBinding
import com.snapco.techlife.extensions.AccountManager
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.login.SaveAccActivity
import com.snapco.techlife.ui.view.activity.login.SaveListAccActivity
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import io.getstream.chat.android.client.ChatClient

class MenuProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuProfileBinding
    private val client: ChatClient by lazy { ChatClient.instance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMenuProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupToolbar()
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.textView33.setOnClickListener {
            startActivity<ChangePasswordActivity>()
        }
        binding.btnSigout.setOnClickListener {
            val accountManager = AccountManager(this@MenuProfileActivity)
            val idUser = UserDataHolder.getUserId()
            val savedAccount = accountManager.getAccounts().find { it.id == idUser }
            if (savedAccount?.state == "false") {
                showSaveLoginDialog()
            } else {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showSaveLoginDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Lưu thông tin đăng nhập")
            .setMessage(
                "Chúng tôi sẽ lưu thông tin đăng nhập để bạn không cần nhập" +
                    " trên thiết bị của mình vào lần đăng nhập sau",
            ).setPositiveButton("Lưu") { dialog, _ ->
                updateAccountState("true")
                dialog.dismiss()
                showLogoutConfirmationDialog()
            }.setNegativeButton("Lúc khác") { dialog, _ ->
                updateAccountState("false")
                dialog.dismiss()
                showLogoutConfirmationDialog()
            }.show()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Đăng xuất khỏi tài khoản của bạn?")
            .setPositiveButton("Đăng xuất") { dialog, _ ->
                dialog.dismiss()
                updateAccountStatus("false")
                logout()
            }.setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun updateAccountState(state: String) {
        UserDataHolder.getUserId()?.let { userId ->
            GetUserResponseHolder.getGetUserResponse()?.let {
                val accountManager = AccountManager(this)
                accountManager.getAccounts().find { it.id == userId }?.let { savedAccount ->
                    val updatedAccount = savedAccount.copy(state = state, status = "false")
                    accountManager.updateAccount(updatedAccount)
                    Log.d("AccountManager", "Account updated: $updatedAccount")
                } ?: Log.d("AccountManager", "Account not found")
                Log.d("AccountManager", "Accounts after adding: ${accountManager.getAccounts()}")
            }
        }
    }

    private fun updateAccountStatus(status: String) {
        val accountManager = AccountManager(this)
        accountManager.getAccounts().find { it.id == UserDataHolder.getUserId() }?.let { savedAccount ->
            val updatedAccount = savedAccount.copy(status = status)
            accountManager.updateAccount(updatedAccount)
            Log.d("AccountManager", "Account updated: $updatedAccount")
        } ?: Log.d("AccountManager", "Account not found")
    }

    private fun logout() {
        val accountManager = AccountManager(this)
        val savedAccounts = accountManager.getAccounts()

        if (savedAccounts.size == 1) {
            startActivity<SaveAccActivity>()
        } else {
            startActivity<SaveListAccActivity>()
        }
        disConnectGetStream()
        finish()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar5)
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

    fun disConnectGetStream() {
        client.disconnect(flushPersistence = false).enqueue { disconnectResult ->
            if (disconnectResult.isSuccess) {
                // Nếu ngắt kết nối thành công, hiển thị thông báo đăng xuất
                Log.d("DISCONECTCHAT", "disConnectGetStream: ok")
            } else {
                // Xử lý lỗi khi ngắt kết nối
                Log.d("DISCONECTCHAT", "disConnectGetStream: fail")
            }
        }
    }
}
