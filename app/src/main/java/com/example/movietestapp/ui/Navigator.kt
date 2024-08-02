package com.example.movietestapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.movietestapp.MainActivity
import com.example.movietestapp.R
import com.example.movietestapp.ui.detail.MovieDetailFragment
import com.example.movietestapp.ui.favorites.FavoritesFragment
import com.example.movietestapp.ui.intro.IntroFragment
import com.example.movietestapp.ui.login.LoginFragment
import com.example.movietestapp.ui.home.HomeMoviesFragment
import com.example.movietestapp.ui.profile.ProfileFragment
import com.example.movietestapp.ui.register.RegisterFragment
import com.example.movietestapp.ui.splash.SplashFragment

class Navigator(mainActivity: MainActivity) : AppCompatActivity() {
    private val activity = mainActivity
    private val fragmentManager: FragmentManager = mainActivity.supportFragmentManager
    private var fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

    companion object {
        private lateinit var instance: Navigator

        fun create(activity: MainActivity) {
            instance = Navigator(activity)
        }

        fun getInstance(): Navigator {
            return instance
        }
    }

    private fun beginTransaction() {
        fragmentTransaction = fragmentManager.beginTransaction()
    }

    fun openSplashFragment() {
        fragmentTransaction.add(R.id.fragmentHolder, SplashFragment()).commit()
        setNavigationBarInvisible()
    }

    fun openIntroFragment() {
        beginTransaction()
        fragmentTransaction.replace(R.id.fragmentHolder, IntroFragment()).commit()
        setNavigationBarInvisible()
    }

    fun openRegisterFragment() {
        beginTransaction()
        fragmentTransaction.replace(R.id.fragmentHolder, RegisterFragment()).commit()
        setNavigationBarInvisible()
    }

    fun openLoginFragment() {
        beginTransaction()
        fragmentTransaction.replace(R.id.fragmentHolder, LoginFragment()).commit()
        setNavigationBarInvisible()
    }

    fun openHomeMoviesFragment() {
        beginTransaction()
        fragmentTransaction.replace(R.id.fragmentHolder, HomeMoviesFragment()).commit()
        setNavigationBarVisible()
    }

    fun openMovieDetailFragment(idOfMovie: Int) {
        beginTransaction()
        val movieDetailFragment = constructMovieDetailFragment(idOfMovie)
        fragmentTransaction.replace(R.id.fragmentHolder, movieDetailFragment).commit()
        setNavigationBarVisible()
    }

    fun openFavoritesFragment() {
        beginTransaction()
        fragmentTransaction.replace(R.id.fragmentHolder, FavoritesFragment()).commit()
        setNavigationBarVisible()
    }

    fun openProfileFragment() {
        beginTransaction()
        fragmentTransaction.replace(R.id.fragmentHolder, ProfileFragment()).commit()
        setNavigationBarVisible()
    }

    fun setNavigationBarVisible() {
        activity.binding.navigationBar.visibility = View.VISIBLE
    }

    fun setNavigationBarInvisible() {
        activity.binding.navigationBar.visibility = View.INVISIBLE
    }

    private fun constructMovieDetailFragment(idOfMovie: Int): MovieDetailFragment {
        val bundle = Bundle()
        bundle.putInt("movie_id", idOfMovie)

        val movieDetailFragment = MovieDetailFragment()
        movieDetailFragment.arguments = bundle

        return movieDetailFragment
    }
}