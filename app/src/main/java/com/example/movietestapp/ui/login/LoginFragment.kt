package com.example.movietestapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.movietestapp.R
import com.example.movietestapp.data.dto.UserData
import com.example.movietestapp.databinding.FragmentLoginBinding
import com.example.movietestapp.ui.Navigator
import com.example.movietestapp.util.NetworkUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {
//    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        setListeners()
        return view
    }

    private fun setListeners() {
        binding.loginButton.setOnClickListener {
            if (areFieldsEmpty()) {
                showSnackbar(requireView(), getString(R.string.message_username_password_empty))
            } else if (!NetworkUtils.isNetworkAvailable(requireContext())) {
                showSnackbar(requireView(), getString(R.string.message_no_network))
            } else {
                val username = binding.usernameInputField.text.toString()
                val password = binding.passwordInputField.text.toString()

                databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        loginUser(dataSnapshot, username, password)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(requireContext(), "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.textViewDontHaveAccount.setOnClickListener{
            Navigator.getInstance().openRegisterFragment()
        }
    }

//    private fun loginUser(dataSnapshot: DataSnapshot, username: String, password: String) {
//        if (dataSnapshot.exists()) {
//            for (userSnapshot in dataSnapshot.children) {
//                val userData = userSnapshot.getValue(UserData::class.java)
//
//                if (userData != null && userData.password.equals(password)) {
//                    showSnackbar(requireView(), getString(R.string.message_login_successful))
//                    Navigator.getInstance().openHomeMoviesFragment()
//                } else {
//                    showSnackbar(requireView(), getString(R.string.message_login_failed))
//                }
//            }
//        } else {
//            showSnackbar(requireView(), getString(R.string.message_login_failed))
//        }
//    }

    private fun loginUser(dataSnapshot: DataSnapshot, username: String, password: String) {
        if (dataSnapshot.exists()) {
            for (userSnapshot in dataSnapshot.children) {
                val userData = userSnapshot.getValue(UserData::class.java)

                if (userData != null && userData.password.equals(password)) {
                    saveCurrentUser(userData)
                    showSnackbar(requireView(), getString(R.string.message_login_successful))
                    Navigator.getInstance().openHomeMoviesFragment()
                } else {
                    showSnackbar(requireView(), getString(R.string.message_login_failed))
                }
            }
        } else {
            showSnackbar(requireView(), getString(R.string.message_login_failed))
        }
    }

    private fun saveCurrentUser(userData: UserData) {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userData.id)
        editor.putString("username", userData.username)
        editor.apply()
    }


    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun areFieldsEmpty(): Boolean {
        val usernameInputField = binding.usernameInputField
        val passwordInputField = binding.passwordInputField

        if (usernameInputField.text.toString().isEmpty()) {
            usernameInputField.error = getString(R.string.invalid_username)
            return true
        }

        if (passwordInputField.text.toString().isEmpty()) {
            passwordInputField.error = getString(R.string.invalid_password)
            return true
        }
        return false
    }
}