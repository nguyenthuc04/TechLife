package com.snapco.techlife.ui.view.activity.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.UpdateUserRequest
import com.snapco.techlife.databinding.ActivityDetailEditProfileBinding
import com.snapco.techlife.extensions.isVisible
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class DetailEditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEditProfileBinding
    private val userViewModel: UserViewModel by viewModels()
    private var originalText: String? = null
    private var hasChanges = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupToolbar()
        handleIntentData()
        setupTextWatchers()
        setupDoneButton()
        setupBackButton()
        observeUpdateUserResponse()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleIntentData() =
        binding.apply {
            val title = intent.getStringExtra("TITLE")
            val viewToShow = intent.getStringExtra("VIEW_TO_SHOW")
            val text = intent.getStringExtra("TEXT")

            toolbarTitle.text = title
            originalText = text

            when (viewToShow) {
                "textInputLayoutEmail" -> {
                    textInputLayoutEmail.visible()
                    editConfirm.setText(text)
                    charCountTextView.text = "${text?.length} / 150"
                }
                "editTextLongText" -> {
                    editTextLongText.visible()
                    charCountTextView.visible()
                    editTextLongText.setText(text)
                    charCountTextView.text = "${text?.length} / 150"
                }
            }
        }

    private fun setupTextWatchers() {
        val textWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    val currentText = s.toString()
                    hasChanges = currentText != originalText
                    updateDoneButtonState()
                    binding.charCountTextView.text = "${currentText.length} / 150"
                }

                override fun afterTextChanged(s: Editable?) {}
            }

        binding.editConfirm.addTextChangedListener(textWatcher)
        binding.editTextLongText.addTextChangedListener(textWatcher)
    }

    private fun updateDoneButtonState() {
        binding.btnDone.apply {
            isEnabled = hasChanges
            setTextColor(resources.getColor(if (hasChanges) R.color.blueLogo else R.color.stream_gray_dark))
        }
    }

    private fun setupDoneButton() {
        binding.btnDone.setOnClickListener {
            val newText =
                if (binding.textInputLayoutEmail.isVisible()) {
                    binding.editConfirm.text.toString()
                } else {
                    binding.editTextLongText.text.toString()
                }

            val field = intent.getStringExtra("FIELD") ?: return@setOnClickListener

            if (field == "bio") {
                updateUserBio(newText)
            } else {
                showUpdateDialog(newText, field)
            }
        }
    }

    private fun updateUserBio(newText: String) {
        val userId = UserDataHolder.getUserId() ?: return
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            val updateUserRequest =
                UpdateUserRequest(
                    account = response.user.account,
                    password = response.user.password,
                    birthday = response.user.birthday,
                    name = response.user.name,
                    nickname = response.user.nickname,
                    bio = newText,
                    avatar = response.user.avatar,
                    accountType = response.user.accountType,
                )
            GetUserResponseHolder.setGetUserResponse(
                response.copy(user = response.user.copy(bio = newText)),
            )
            userViewModel.updateUser(userId, updateUserRequest)
        }
    }

    private fun showUpdateDialog(
        newText: String,
        field: String,
    ) {
        AlertDialog
            .Builder(this)
            .setTitle("Thông báo")
            .setMessage("Bạn có chắc muốn đổi thành $newText")
            .setPositiveButton("Đổi tên") { _, _ ->
                updateUserField(newText, field)
            }.setNegativeButton("Hủy", null)
            .show()
    }

    private fun updateUserField(
        newText: String,
        field: String,
    ) {
        val userId = UserDataHolder.getUserId() ?: return
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            val updateUserRequest =
                UpdateUserRequest(
                    account = response.user.account,
                    password = response.user.password,
                    birthday = response.user.birthday,
                    name = if (field == "name") newText else response.user.name,
                    nickname = if (field == "nickname") newText else response.user.nickname,
                    bio = response.user.bio,
                    avatar = response.user.avatar,
                    accountType = response.user.accountType,
                )
            GetUserResponseHolder.setGetUserResponse(
                response.copy(
                    user =
                        response.user.copy(
                            name = updateUserRequest.name,
                            nickname = updateUserRequest.nickname,
                        ),
                ),
            )
            userViewModel.updateUser(userId, updateUserRequest)
        }
    }

    private fun setupBackButton() {
        binding.toolbar5.setNavigationOnClickListener {
            if (hasChanges) {
                showDiscardChangesDialog()
            } else {
                finish()
            }
        }
    }

    private fun showDiscardChangesDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Bỏ thay đổi")
            .setMessage("Nếu quay lại bây giờ thì những gì bạn vừa thay đổi sẽ mất.")
            .setPositiveButton("Bỏ thay đổi") { _, _ -> finish() }
            .setNegativeButton("Tiếp tục chỉnh sửa", null)
            .show()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar5)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }

    private fun observeUpdateUserResponse() {
        userViewModel.updateUserResponse.observe(this) { response ->
            response?.let {
                if (response.message == "Người dùng đã được cập nhật thành công!") {
                    finish()
                } else {
                    showUpdateErrorDialog()
                }
            }
        }
    }

    private fun showUpdateErrorDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Lỗi")
            .setMessage("Cập nhật thất bại. Vui lòng thử lại.")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
