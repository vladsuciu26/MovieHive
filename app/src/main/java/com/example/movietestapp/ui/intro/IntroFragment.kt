package com.example.movietestapp.ui.intro

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movietestapp.databinding.FragmentIntroBinding
import com.example.movietestapp.ui.Navigator

class IntroFragment : Fragment() {
    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        val view = binding.root
        setListeners()
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.loginIntroButton.setOnTouchListener {view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Navigator.getInstance().openLoginFragment()
            }
            false
        }

        binding.registerIntroButton.setOnTouchListener {view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Navigator.getInstance().openRegisterFragment()
            }
            false
        }
    }
}