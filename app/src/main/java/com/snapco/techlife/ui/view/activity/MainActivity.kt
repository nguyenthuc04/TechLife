package com.snapco.techlife.ui.view.activity

import HomeFragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityMainBinding
import com.snapco.techlife.ui.view.fragment.camera.CameraFragment
import com.snapco.techlife.ui.view.fragment.profile.ProfileFragment
import com.snapco.techlife.ui.view.fragment.reels.AddReelFragment
import com.snapco.techlife.ui.view.fragment.reels.ReelsFragment
import com.snapco.techlife.ui.view.fragment.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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
            .replace(R.id.frameLayout,HomeFragment())
            .commit()

        binding.bottomNavigationView.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.menu_home -> HomeFragment()
                R.id.menu_search -> SearchFragment()
                R.id.menu_camera -> CameraFragment()
                R.id.menu_reels -> AddReelFragment()
                R.id.menu_profile -> ProfileFragment()
                else -> throw IllegalArgumentException("Unknown menu item")
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
            true
        }
    }
}