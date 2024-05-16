package com.example.movietestapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movietestapp.databinding.ActivityMainBinding
import com.example.movietestapp.ui.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Navigator.create(this)
        Navigator.getInstance().openSplashFragment()
//        setListenersForNavigationBar()
    }

//    private fun setListenersForNavigationBar() {
//        binding.navigationBar.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_profile -> {
//                    Navigator.getInstance().openProfileFragment()
//                    true
//                }
//
//                R.id.navigation_devices -> {
//                    Navigator.getInstance().openDevicesFragment()
//                    true
//                }
//
//                R.id.navigation_my_devices -> {
//                    Navigator.getInstance().openMyDevicesFragment()
//                    true
//                }
//
//                R.id.navigation_device_management -> {
//                    Navigator.getInstance().openDeviceManagementFragment()
//                    true
//                }
//
//                else -> false
//            }
//        }
//    }
}