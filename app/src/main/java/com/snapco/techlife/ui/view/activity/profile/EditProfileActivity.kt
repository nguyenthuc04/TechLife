package com.snapco.techlife.ui.view.activity.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.GetUserResponse
import com.snapco.techlife.databinding.ActivityEditProfileBinding
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetItem
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetProfielAvatar
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupToolbar()
        setUpOnClick()
        setupActivityResultLauncher()
        GetUserResponseHolder.getGetUserResponse()?.let { updateUI(it) }
    }

    private fun setupActivityResultLauncher() {
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // Đóng BottomSheet nếu cần thiết
                    supportFragmentManager.findFragmentByTag("BottomSheetProfielAvatar")?.let {
                        (it as BottomSheetProfielAvatar).dismiss()
                    }
                }
            }
    }

    private fun onClickLibrary() {
        val intent = Intent(this, CustomGalleryActivity::class.java)
        galleryLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setUpOnClick() =
        binding.apply {
            imgAvatar2.setOnClickListener {
                val items =
                    listOf(
                        BottomSheetItem(
                            R.drawable.ic_icon_reel_default,
                            "Chọn trong thư viện",
                        ) { onClickLibrary() },
                        BottomSheetItem(
                            R.drawable.ic_icon_reel_default,
                            "Chụp ảnh",
                        ) { takeAphoto() },
                    )

                val bottomSheet =
                    BottomSheetProfielAvatar().apply {
                        setItems(items)
                    }
                val idUser = UserDataHolder.getUserId() ?: ""
                userViewModel.updateAvatarUserChat(idUser, items.toString())
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }
            textView23.setOnClickListener {
                startDetailEditProfileActivity(
                    "Tên",
                    "textInputLayoutEmail",
                    textView23.text.toString(),
                    "nickname",
                )
            }
            textView25.setOnClickListener {
                startDetailEditProfileActivity(
                    "Tên người dùng",
                    "textInputLayoutEmail",
                    textView25.text.toString(),
                    "name",
                )
            }
            textView27.setOnClickListener {
                startDetailEditProfileActivity(
                    "Tiểu sử",
                    "editTextLongText",
                    textView27.text.toString(),
                    "bio",
                )
            }
        }

    private fun takeAphoto() {
        val intent = Intent(this, CustomCameraActivity::class.java)
        galleryLauncher.launch(intent)
    }

    private fun startDetailEditProfileActivity(
        title: String,
        viewToShow: String,
        text: String,
        field: String,
    ) {
        val intent =
            Intent(this, DetailEditProfileActivity::class.java).apply {
                putExtra("TITLE", title)
                putExtra("VIEW_TO_SHOW", viewToShow)
                putExtra("TEXT", text)
                putExtra("FIELD", field)
            }
        startActivity(intent)
    }

    private fun loadData() {
        UserDataHolder.getUserId()?.let { userViewModel.getUser(it) }
        userViewModel.userResponse.observe(this) { response ->
            response?.let { updateUI(it) }
        }
    }

    private fun updateUI(response: GetUserResponse) =
        binding.apply {
            imgAvatar2.loadImage(response.user.avatar)
            textView23.text = response.user.nickname
            textView25.text = response.user.name
            textView27.text = response.user.bio
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
}
