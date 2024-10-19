package com.snapco.techlife.ui.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.snaplife.ui.view.fragment.camera.CameraFragment
import com.snapco.snaplife.ui.view.fragment.home.HomeFragment
import com.snapco.snaplife.ui.view.fragment.profile.ProfileFragment
import com.snapco.snaplife.ui.view.fragment.reels.ReelsFragment
import com.snapco.snaplife.ui.view.fragment.search.SearchFragment
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityMainBinding

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
    }
}