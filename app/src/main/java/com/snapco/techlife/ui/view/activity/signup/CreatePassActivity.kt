package com.snapco.techlife.ui.view.activity.signup

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.User
import com.snapco.techlife.databinding.ActivityCreatePassBinding
import com.snapco.techlife.extensions.isValidPassword
import com.snapco.techlife.extensions.showToast
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.viewmodel.SignUpDataHolder

class CreatePassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePassBinding
    private val signUpDataHolder: SignUpDataHolder by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreatePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        binding.btnNext.setOnClickListener {
            val pass = binding.editPass.text.toString()
            if (pass.isEmpty()) {
                showToast("Vui lòng nhập mật khẩu")
                return@setOnClickListener
            }
            if (!pass.isValidPassword()) {
                showToast(
                    "Có ít nhất một chữ cái viết hoa.\n" +
                        "Có ít nhất một chữ cái viết thường.\n" +
                        "Có ít nhất một chữ số.\n" +
                        "Có ít nhất một ký tự đặc biệt (trong các ký tự @\$!%*?&).\n" +
                        "Độ dài tối thiểu là 6 ký tự.",
                )
                return@setOnClickListener
            }
            val user = User(password = pass)
            signUpDataHolder.setUser(user)
            startActivity<BirthdayActivity>()
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
