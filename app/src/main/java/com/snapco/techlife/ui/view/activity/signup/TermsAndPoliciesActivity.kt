package com.snapco.techlife.ui.view.activity.signup

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityTermsAndPoliciesBinding
import com.snapco.techlife.extensions.setHighlightedText
import com.snapco.techlife.extensions.startActivity

class TermsAndPoliciesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsAndPoliciesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTermsAndPoliciesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()
        setupToolbar()
        binding.btnNext.setOnClickListener {
            startActivity<AvatarActivity>()
        }
        customText()
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
                "Những người dùng dịch vụ của chúng tôi có thể đã tải thông tin liên hệ của bạn lên TechLife.\nTìm hiểu thêm",
            highlights = listOf("Tìm hiểu thêm" to Color.BLUE),
        )
        binding.textView13.setHighlightedText(
            fullText = "Bạn cũng sẽ nhận được email của chúng tôi và có thể chọn ngừng nhận bất cứ lúc nào. Tìm hiểu thêm",
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
