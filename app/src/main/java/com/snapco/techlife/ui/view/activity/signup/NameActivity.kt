package com.snapco.techlife.ui.view.activity.signup

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityNameBinding
import com.snapco.techlife.extensions.getTag
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.viewmodel.SignUpDataHolder

class NameActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:property-naming")
    private val TAG = getTag()
    private lateinit var binding: ActivityNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()
        setupToolbar()
        binding.btnNext.setOnClickListener {
            val name = binding.editTextText.text.toString()
            if (name.isEmpty()) {
                return@setOnClickListener
            }
            val oldUser = SignUpDataHolder.getUser()
            Log.d(TAG, "oldUser: $oldUser")
            val user = oldUser?.copy(name = name)
            user?.let { SignUpDataHolder.setUser(it) }
            startActivity<NickNameActivity>()
        }
    }

    private fun applyWindowInsets() {
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
