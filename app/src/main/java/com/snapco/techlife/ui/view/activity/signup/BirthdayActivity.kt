package com.snapco.techlife.ui.view.activity.signup

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityBirthdayBinding
import com.snapco.techlife.extensions.getTag
import com.snapco.techlife.extensions.setHighlightedText
import com.snapco.techlife.extensions.showCustomAlertDialogINeedAcc
import com.snapco.techlife.extensions.startActivity
import com.snapco.techlife.ui.view.activity.login.LoginActivity
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetDialogBirthdayFragment
import com.snapco.techlife.ui.viewmodel.SignUpDataHolder

class BirthdayActivity :
    AppCompatActivity(),
    BottomSheetListener {
    @Suppress("ktlint:standard:property-naming")
    private val TAG = getTag()
    private lateinit var binding: ActivityBirthdayBinding
    private var formattedDate: String = ""

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBirthdayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()
        setupToolbar()
        setupBirthdayPicker()
        binding.btnAccount.setOnClickListener {
            showCustomAlertDialogINeedAcc(
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
        binding.textView.setHighlightedText(
            fullText =
                "Hãy sử dụng ngày sinh của chính bạn, ngay cả khi tài khoản này dành" +
                    " cho doanh nghiệp, thú cưng hay chủ đề khác. Thông tin này sẽ không hiển" +
                    " thị với bất kỳ ai trừ khi bạn chọn chia sẻ. Tại sao tôi cần cung cấp ngày sinh của mình?",
            highlights = listOf("Tại sao tôi cần cung cấp ngày sinh của mình?" to Color.BLUE),
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBirthdayPicker() {
        binding.edtBirthday.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showBottomSheetPicker()
                return@setOnTouchListener true
            }
            false
        }
    }

    private fun showBottomSheetPicker() {
        val bottomSheetFragment = BottomSheetDialogBirthdayFragment()
        bottomSheetFragment.listener = this
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
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

    override fun onDateSet(
        formattedDate: String,
        birthdayDate: String,
        age: Int,
    ) {
        this.formattedDate = formattedDate
        binding.edtBirthday.setText(birthdayDate)
        binding.textInputLayoutEmail.hint = "Ngày sinh ($age tuổi)"
        binding.btnNext.isEnabled = age >= 10
        binding.txtWR.visibility =
            if (age >= 10) android.view.View.GONE else android.view.View.VISIBLE
        binding.btnNext.setOnClickListener {
            val oldUser = SignUpDataHolder.getUser()
            Log.d(TAG, "oldUser: $oldUser")
            val user = oldUser?.copy(birthday = formattedDate)
            user?.let {
                Log.d(TAG, "user: $it")
                SignUpDataHolder.setUser(it)
            }
            startActivity<NameActivity>()
        }
    }
}

interface BottomSheetListener {
    fun onDateSet(
        formattedDate: String,
        birthdayDate: String,
        age: Int,
    )
}
