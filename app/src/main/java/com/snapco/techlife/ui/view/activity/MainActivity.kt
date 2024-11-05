package com.snapco.techlife.ui.view.activity

import HomeFragment
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.commit


import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityMainBinding
import com.snapco.techlife.ui.view.fragment.camera.CameraFragment
import com.snapco.techlife.ui.view.fragment.profile.ProfileFragment
import com.snapco.techlife.ui.view.fragment.reels.ReelsFragment
import com.snapco.techlife.ui.view.fragment.search.SearchFragment
import com.snapco.techlife.ui.viewmodel.UserDataHolder
import com.snapco.techlife.ui.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, HomeFragment())
            .commit()

        binding.bottomNavigationView.setOnItemSelectedListener {

            val fragment = when (it.itemId) {
                R.id.menu_home -> HomeFragment()
                R.id.menu_search -> SearchFragment()
                R.id.menu_camera -> {
                    if (checkPermissions()) {
                        CameraFragment()
                    } else {
                        requestPermissions()
                        null
                    }
                }
                R.id.menu_reels -> ReelsFragment()
                R.id.menu_profile -> ProfileFragment()
                else -> throw IllegalArgumentException("Unknown menu item")
            }

            fragment?.let {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, it)
                    .commit()
            }
            true
        }
        fetchUserData()
    }

    private fun fetchUserData() {
        lifecycleScope.launch(Dispatchers.IO) {
            UserDataHolder.getUserId()?.let { userId ->
                userViewModel.getUser(userId)
            }
        }
    }
    private fun checkPermissions(): Boolean {
        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val recordAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return writePermission == PackageManager.PERMISSION_GRANTED &&
                readPermission == PackageManager.PERMISSION_GRANTED &&
                cameraPermission == PackageManager.PERMISSION_GRANTED &&
                recordAudioPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED })) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.frameLayout, CameraFragment::class.java, null)
                    addToBackStack(null)
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
