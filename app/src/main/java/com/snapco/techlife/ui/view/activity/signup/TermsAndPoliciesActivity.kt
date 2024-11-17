package com.snapco.techlife.ui.view.activity.signup

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityTermsAndPoliciesBinding
import com.snapco.techlife.extensions.getTag
import com.snapco.techlife.extensions.setHighlightedText
import com.snapco.techlife.extensions.showCustomAlertDialog
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.login.LoginActivity
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.SignUpDataHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class TermsAndPoliciesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsAndPoliciesBinding
    val TAG = getTag()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTermsAndPoliciesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()
        setupToolbar()
        binding.btnNext.setOnClickListener {
            createAccount()
        }
        customText()
        binding.btnBack.setOnClickListener {
            showCustomAlertDialog(
                this,
                title = "Bạn đã có tài khoản ư?",
                message = "",
                positiveButtonText = "Đăng nhập",
                negativeButtonText = "Tiếp tục tạo tài khoản",
                positiveAction = {
                    startActivity<LoginActivity>()
                },
                negativeAction = {
                },
            )
        }

        userViewModel.createUserResponse.observe(
            this,
            Observer { response ->
                if (response != null) {
                    Log.d(TAG, "SignUp successful: $response")
                    val user = response.user

                    if (user != null) {
                        UserDataHolder.setUserData(user.id, user.account, user.name, user.avatar)
                        Log.d(TAG, "User data: ${UserDataHolder.getUserId()}")

                        val tokenChat = response.streamToken
                        if (tokenChat != null) {
                            userViewModel.connectChat(user.id, user.name, tokenChat,"")
                            Log.d(TAG, "onCreate:  " + tokenChat)
                        }
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.btnNext.text = "Tôi đồng ý"
                        binding.progressBar.visibility = View.GONE
                        startActivity<LoginActivity>()
                    }, 2000)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.btnNext.text = "Tôi đồng ý"
                        binding.progressBar.visibility = View.GONE
                        Log.d(TAG, "Login failed")
                    }, 1000)
                }
            },
        )
    }

    private fun createAccount() {
        Log.d(TAG, SignUpDataHolder.getUser().toString())
        binding.btnNext.text = ""
        binding.progressBar.visibility = View.VISIBLE
        val oldUser = SignUpDataHolder.getUser()

        val user =
            oldUser?.copy(
                accountType = "mentee",
                bio = "null",
                avatar = "https://www.gravatar.com/avatar/?d=mp&f=y",
            )
        user?.let {
            Log.d(TAG, "user: $it")
            SignUpDataHolder.setUser(it)
        }
        val newUser = SignUpDataHolder.getUser()
        if (newUser != null) {
            userViewModel.createUser(newUser)
        }
    }

    private fun customText() {
        binding.textView8.setHighlightedText(
            fullText =
                "Bằng việc đăng ký, bạn đồng ý với Điều khoản, Chính sách quyền riêng tư và" +
                    " Chính sách cookie của TechLife.",
            highlights =
                listOf(
                    "Điều khoản" to Color.BLUE,
                    "Chính sách quyền riêng tư" to Color.BLUE,
                    "Chính sách cookie" to Color.BLUE,
                ),
        )
        binding.textView9.setHighlightedText(
            fullText =
                "Chúng tôi dùng thông tin của bạn để hiển thị cũng như cá nhân hóa quảng cáo và nội dung" +
                    " thương mại mà bạn có thế sẽ thích. Chúng tôi cũng dùng thông tin đó để nghiên cứu" +
                    " và đối mới, bao gồm cả đế phục vụ hoạt động vì cộng đồng cũng như lợi ích công. Tìm hiểu thêm",
            highlights = listOf("Tìm hiểu thêm" to Color.BLUE),
        )
        binding.textView10.setHighlightedText(
            fullText =
                "Bạn có thể chọn cung cấp thông tin về bản thân mà thông tin này có thể được" +
                    " bảo vệ đặc biệt theo luật quyền riêng tư ở nơi bạn sống. Tìm hiểu thêm",
            highlights = listOf("Tìm hiểu thêm" to Color.BLUE),
        )
        binding.textView11.setHighlightedText(
            fullText = "Bạn có thể truy cập, thay đối hoặc xóa thông tin của mình bất cứ lúc nào. Tìm hiểu thêm",
            highlights = listOf("Tìm hiểu thêm" to Color.BLUE),
        )
        binding.textView12.setHighlightedText(
            fullText =
                "Những người dùng dịch vụ của chúng tôi có thể đã tải thông tin liên hệ của bạn lên" +
                    " TechLife.\nTìm hiểu thêm",
            highlights = listOf("Tìm hiểu thêm" to Color.BLUE),
        )
        binding.textView13.setHighlightedText(
            fullText =
                "Bạn cũng sẽ nhận được email của chúng tôi và có thể chọn ngừng nhận bất cứ " +
                    "lúc nào. Tìm hiểu thêm",
            highlights = listOf("Tìm hiểu thêm" to Color.BLUE),
        )
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
