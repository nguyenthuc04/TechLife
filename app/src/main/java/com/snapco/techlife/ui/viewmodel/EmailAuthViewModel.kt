package com.snapco.techlife.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapco.techlife.extensions.GmailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailAuthViewModel(
    private val context: Context,
) : ViewModel() {
    private val gmailSender = GmailSender("tretrauzxx@gmail.com", "knhe erqn wugp seuh")
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("otp_prefs", Context.MODE_PRIVATE)

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> get() = _authResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun sendVerificationEmail(to: String) {
        val otpCode = generateOtp()
        saveOtpCode(otpCode)
        val subject = "Your OTP Code"
        val text = "Your OTP code is: $otpCode"

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    gmailSender.sendEmail(to, subject, text)
                }
                _authResult.postValue(true)
            } catch (e: Exception) {
                _authResult.postValue(false)
                _error.postValue(e.message)
            }
        }
    }

    fun verifyOtp(inputOtp: String): Boolean {
        val storedOtp = getOtpCode()
        Log.d("EmailAuthViewModel", "Input OTP: $inputOtp, Stored OTP: $storedOtp")
        return inputOtp == storedOtp
    }

    private fun generateOtp(): String {
        val otp = (100000..999999).random().toString()
        Log.d("EmailAuthViewModel", "Generated OTP: $otp")
        return otp
    }

    private fun saveOtpCode(otpCode: String) {
        sharedPreferences.edit().putString("otp_code", otpCode).apply()
    }

    private fun getOtpCode(): String? = sharedPreferences.getString("otp_code", null)
}
