package com.snapco.techlife.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otaliastudios.cameraview.controls.Facing

class CameraViewModel : ViewModel(){

    private val _facing = MutableLiveData(Facing.BACK)
    val facing: LiveData<Facing> = _facing

    private val _isRecording = MutableLiveData(false)
    val isRecording: LiveData<Boolean> = _isRecording

    fun switchCamera() {
        _facing.value = if (_facing.value == Facing.BACK) Facing.FRONT else Facing.BACK
    }

    fun startRecording() {
        _isRecording.value = true
    }

    fun stopRecording() {
        _isRecording.value = false
    }
}