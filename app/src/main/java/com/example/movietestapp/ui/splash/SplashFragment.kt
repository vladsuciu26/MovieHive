package com.example.movietestapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.movietestapp.R
import com.example.movietestapp.databinding.FragmentSplashBinding
import com.example.movietestapp.ui.Navigator

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
        setAnimationsForLogoAndText()
        return view
    }

    private fun setAnimationsForLogoAndText() {
        val animationTop = AnimationUtils.loadAnimation(requireContext(), R.anim.animation_top)
        val animationBottom =
            AnimationUtils.loadAnimation(requireContext(), R.anim.animation_bottom)
        setListenerToBottomAnimation(animationBottom)
        binding.splashLogo.animation = animationTop
        binding.splashText.animation = animationBottom
    }

    private fun setListenerToBottomAnimation(animationBottom: Animation?) {
        animationBottom?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Animation has ended, perform the fragment transaction
                navigateToIntro()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun navigateToIntro() {
        Navigator.getInstance().openIntroFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}