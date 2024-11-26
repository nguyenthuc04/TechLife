package com.snapco.techlife.ui.view.activity.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.User
import com.snapco.techlife.extensions.AccountManager
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.MainActivity
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class WelcomeActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        setupWindowInsets()
        setupLoginObserver()
        Handler(Looper.getMainLooper()).postDelayed({
            checkAccountsAndNavigate()
        }, 2000)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupLoginObserver() {
        userViewModel.loginNoHashResponse.observe(this) { response ->
            response?.let {
                handleLoginResponse(it.user, it.streamToken.toString())
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
            accountManager.getAccounts().find { acc -> acc.id == it.id }?.let { savedAccount ->
                val updatedAccount = savedAccount.copy(status = "true")
                accountManager.updateAccount(updatedAccount)
                Log.d("AccountManager", "Account updated: $updatedAccount")
            } ?: Log.d("AccountManager", "Account not found")
            userViewModel.connectChat(it.id, it.account, tokenChat, it.avatar)
            userViewModel.updateLastLogin(it.id)
        }
        startActivity<MainActivity>()
    }

    private fun checkAccountsAndNavigate() {
        val accountManager = AccountManager(this)
        val savedAccounts = accountManager.getAccounts()

        when {
            savedAccounts.isNullOrEmpty() -> navigateToLogin()
            savedAccounts.any { it.status == "true" } -> {
                val activeAccount = savedAccounts.find { it.status == "true" }
                activeAccount?.let { userViewModel.loginNoHash(it.account, it.password!!) }
            }
            savedAccounts.size == 1 -> startActivity<SaveAccActivity>()
            else -> startActivity<SaveListAccActivity>()
        }
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("hideBackButton", true)
        startActivity(intent)
    }
}
