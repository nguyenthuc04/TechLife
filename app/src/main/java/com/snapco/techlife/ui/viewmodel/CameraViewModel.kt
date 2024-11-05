package com.snapco.techlife.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otaliastudios.cameraview.controls.Facing

class CameraViewModel : ViewModel(){

    private val _facing = MutableLiveData(Facing.BACK)
    val facing: LiveData<Facing> = _facing

    private val _isRecording = MutableLiveData(false)
    val isRecording: LiveData<Boolean> = _isRecording

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private var startTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            val elapsedMillis = SystemClock.elapsedRealtime() - startTime
            val seconds = (elapsedMillis / 1000).toInt() % 60
            val minutes = (elapsedMillis / 1000 / 60).toInt()
            _timerText.postValue(String.format("%02d:%02d", minutes, seconds))
            handler.postDelayed(this, 1000)
        }
    }

    fun switchCamera() {
        _facing.value = if (_facing.value == Facing.BACK) Facing.FRONT else Facing.BACK
    }

    fun startRecording() {
        _isRecording.value = true
        startTime = SystemClock.elapsedRealtime()
        handler.post(timerRunnable)
    }

    fun stopRecording() {
        _isRecording.value = false
        handler.removeCallbacks(timerRunnable)
        _timerText.postValue("00:00")
    }
    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(timerRunnable)
    }
}