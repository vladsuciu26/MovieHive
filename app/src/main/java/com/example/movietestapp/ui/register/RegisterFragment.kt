package com.example.movietestapp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.movietestapp.R
import com.example.movietestapp.data.dto.UserData
import com.example.movietestapp.databinding.FragmentRegisterBinding
import com.example.movietestapp.ui.Navigator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        val view = binding.root
        setListeners()
        return view
    }

    private fun setListeners() {
        binding.registerButton.setOnClickListener {
            if (areFieldsEmpty()) {
                showSnackbar(requireView(), getString(R.string.message_username_password_empty))
            } else {
                val username = binding.usernameInputField.text.toString()
                val password = binding.passwordInputField.text.toString()
                val confirmPassword = binding.confirmPasswordInputField.text.toString()
                val email = binding.emailInputField.text.toString()

                if (password != confirmPassword) {
                    showSnackbar(requireView(), getString(R.string.passwords_do_not_match))
                } else {
                    databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            registerUser(dataSnapshot, username, password, email)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(requireContext(), "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        binding.textViewAlreadyHaveAccount.setOnClickListener {
            Navigator.getInstance().openLoginFragment()
        }
    }

    private fun registerUser(dataSnapshot: DataSnapshot, username: String, password: String, email: String) {
        if (!dataSnapshot.exists()) {
            val id = databaseReference.push().key
            val userData = UserData(id, username, password, email)
            databaseReference.child(id!!).setValue(userData)
            showSnackbar(requireView(), getString(R.string.register_successful))
            Navigator.getInstance().openLoginFragment()
        } else {
            showSnackbar(requireView(), getString(R.string.user_already_exists))
        }
    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun areFieldsEmpty(): Boolean {
        val usernameInputField = binding.usernameInputField
        val passwordInputField = binding.passwordInputField
        val confirmPasswordInputField = binding.confirmPasswordInputField
        val emailInputField = binding.emailInputField

        if (usernameInputField.text.toString().isEmpty()) {
            usernameInputField.error = getString(R.string.invalid_username)
            return true
        }

        if (passwordInputField.text.toString().isEmpty()) {
            passwordInputField.error = getString(R.string.invalid_password)
            return true
        }

        if (confirmPasswordInputField.text.toString().isEmpty()) {
            confirmPasswordInputField.error = getString(R.string.invalid_password)
            return true
        }

        if (emailInputField.text.toString().isEmpty()) {
            emailInputField.error = getString(R.string.invalid_email)
            return true
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInputField.text.toString()).matches()) {
            emailInputField.error = getString(R.string.invalid_email)
            return true
        }

        return false
    }

}
