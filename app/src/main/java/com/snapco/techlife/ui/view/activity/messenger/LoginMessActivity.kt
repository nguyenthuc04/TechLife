package com.snapco.techlife.ui.view.activity.messenger

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.snapco.techlife.databinding.ActivityLoginMessBinding
import com.snapco.techlife.ui.viewmodel.messenger.LoginMessViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User

class LoginMessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginMessBinding
    private lateinit var loginViewModel: LoginMessViewModel
    private lateinit var client: ChatClient

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this)[LoginMessViewModel::class]
        client = ChatClient.instance()

        binding.btnLogin.setOnClickListener {
            val username = binding.editTextAccount.text.toString()
            loginViewModel.login(username) { streamToken ->
                if (streamToken != null) {
                    client.connectUser(
                        User(id = username, name = username),
                        streamToken
                    ).enqueue { result ->
                        if (result.isSuccess) {
                            Log.d("check123", "onCreate: day la token "+streamToken)
                            Toast.makeText(
                                this@LoginMessActivity,
                                "login success",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@LoginMessActivity, ChannelActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                this@LoginMessActivity,
                                "sign out fail",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    println("Đăng nhập thất bại: không thể lấy token")
                }
            }

        }


    }
}