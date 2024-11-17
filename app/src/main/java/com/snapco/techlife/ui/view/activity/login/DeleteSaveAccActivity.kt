package com.snapco.techlife.ui.view.activity.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.data.model.UserAccount
import com.snapco.techlife.databinding.ActivityDeleteSaveAccBinding
import com.snapco.techlife.extensions.AccountManager
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.adapter.DeleteAccAdapter

class DeleteSaveAccActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteSaveAccBinding
    private lateinit var accountManager: AccountManager
    private lateinit var adapter: DeleteAccAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeleteSaveAccBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupToolbar()
        setupRecyclerView()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar3)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }

    private fun setupRecyclerView() {
        accountManager = AccountManager(this)
        adapter =
            DeleteAccAdapter(accountManager.getAccounts()) { account ->
                deleteAccount(account)
            }
        binding.recycleview.layoutManager = LinearLayoutManager(this)
        binding.recycleview.adapter = adapter
    }

    private fun deleteAccount(account: UserAccount) {
        accountManager.removeAccountById(account.id)
        adapter.updateAccounts(accountManager.getAccounts())
    }

    override fun onSupportNavigateUp(): Boolean {
        handleNavigation()
        return true
    }

    private fun handleNavigation() {
        val savedAccounts = accountManager.getAccounts()
        when {
            savedAccounts.isNullOrEmpty() -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("hideBackButton", true)
                startActivity(intent)
            }
            savedAccounts.size == 1 -> startActivity<SaveAccActivity>()
            else -> onBackPressedDispatcher.onBackPressed()
        }
    }
}
