package com.snapco.techlife.ui.view.activity

import HomeFragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
            val fragment =
                when (it.itemId) {
                    R.id.menu_home -> HomeFragment()
                    R.id.menu_search -> SearchFragment()
                    R.id.menu_camera -> CameraFragment()
                    R.id.menu_reels -> ReelsFragment()
                    R.id.menu_profile -> ProfileFragment()
                    else -> throw IllegalArgumentException("Unknown menu item")
                }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
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
}
