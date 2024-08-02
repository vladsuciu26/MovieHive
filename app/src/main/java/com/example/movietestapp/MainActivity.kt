package com.example.movietestapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movietestapp.databinding.ActivityMainBinding
import com.example.movietestapp.ui.Navigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        Navigator.create(this)
        Navigator.getInstance().openSplashFragment()
        setListenersForNavigationBar()
    }

    private fun setListenersForNavigationBar() {
        binding.navigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    Navigator.getInstance().openHomeMoviesFragment()
                    true
                }

                R.id.navigation_favorites -> {
                    Navigator.getInstance().openFavoritesFragment()
                    true
                }

                R.id.navigation_profile -> {
                    Navigator.getInstance().openProfileFragment()
                    true
                }

                else -> false
            }
        }
    }
}