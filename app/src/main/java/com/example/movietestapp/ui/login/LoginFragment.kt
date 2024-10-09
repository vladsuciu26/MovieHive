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

//class LoginFragment : Fragment() {
//
//    private var _binding: FragmentLoginBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var firebaseDatabase: FirebaseDatabase
//    private lateinit var databaseReference: DatabaseReference
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//        val view = binding.root
//        firebaseDatabase = FirebaseDatabase.getInstance()
//        databaseReference = firebaseDatabase.reference.child("users")
//        setListeners()
//        return view
//    }
//
//    private fun setListeners() {
//        binding.loginButton.setOnClickListener {
//            if (areFieldsEmpty()) {
//                showSnackbar(requireView(), getString(R.string.message_username_password_empty))
//            } else if (!NetworkUtils.isNetworkAvailable(requireContext())) {
//                showSnackbar(requireView(), getString(R.string.message_no_network))
//            } else {
//                val username = binding.usernameInputField.text.toString()
//                val password = binding.passwordInputField.text.toString()
//                val email = binding.emailInputField.text.toString()
//
//                databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
//                    ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        loginUser(dataSnapshot, username, password, email)
//                    }
//                    override fun onCancelled(databaseError: DatabaseError) {
//                        Toast.makeText(requireContext(), "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
//                    }
//                })
//            }
//        }
//
//        binding.textViewDontHaveAccount.setOnClickListener{
//            Navigator.getInstance().openRegisterFragment()
//        }
//
//        binding.textViewForgotPassword.setOnClickListener{
//            Navigator.getInstance().openForgotPasswordFragment()
//        }
//    }
//
//    private fun loginUser(dataSnapshot: DataSnapshot, username: String, password: String, email: String) {
//        if (dataSnapshot.exists()) {
//            for (userSnapshot in dataSnapshot.children) {
//                val userData = userSnapshot.getValue(UserData::class.java)
//
//                if (userData != null && userData.password.equals(password) && userData.email.equals(email)) {
//                    saveCurrentUser(userData)
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
//    private fun saveCurrentUser(userData: UserData) {
//        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("userId", userData.id)
//        editor.putString("username", userData.username)
//        editor.putString("email", userData.email)
//        editor.apply()
//    }
//
//
//    private fun showSnackbar(view: View, message: String) {
//        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
//    }
//
//    private fun areFieldsEmpty(): Boolean {
//        val usernameInputField = binding.usernameInputField
//        val passwordInputField = binding.passwordInputField
//        val emailInputField = binding.emailInputField
//
//        if (usernameInputField.text.toString().isEmpty()) {
//            usernameInputField.error = getString(R.string.invalid_username)
//            return true
//        }
//
//        if (passwordInputField.text.toString().isEmpty()) {
//            passwordInputField.error = getString(R.string.invalid_password)
//            return true
//        }
//
//        if (emailInputField.text.toString().isEmpty()) {
//            emailInputField.error = getString(R.string.invalid_email)
//            return true
//        }
//        return false
//    }
//}


import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

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
                val email = binding.emailInputField.text.toString()

                databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        loginUser(dataSnapshot, username, password, email)
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

        binding.textViewForgotPassword.setOnClickListener{
            Navigator.getInstance().openForgotPasswordFragment()
        }
    }

    private fun loginUser(dataSnapshot: DataSnapshot, username: String, password: String, email: String) {
        if (dataSnapshot.exists()) {
            for (userSnapshot in dataSnapshot.children) {
                val userData = userSnapshot.getValue(UserData::class.java)

                if (userData != null && userData.password.equals(password) && userData.email.equals(email)) {
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
        editor.putString("email", userData.email)
        editor.apply()
    }


    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun areFieldsEmpty(): Boolean {
        val usernameInputField = binding.usernameInputField
        val passwordInputField = binding.passwordInputField
        val emailInputField = binding.emailInputField

        if (usernameInputField.text.toString().isEmpty()) {
            usernameInputField.error = getString(R.string.invalid_username)
            return true
        }

        if (passwordInputField.text.toString().isEmpty()) {
            passwordInputField.error = getString(R.string.invalid_password)
            return true
        }

        if (emailInputField.text.toString().isEmpty()) {
            emailInputField.error = getString(R.string.invalid_email)
            return true
        }
        return false
    }
}



//class LoginFragment : Fragment() {
//    private var _binding: FragmentLoginBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var firebaseDatabase: FirebaseDatabase
//    private lateinit var databaseReference: DatabaseReference
//    private lateinit var firebaseAuth: FirebaseAuth
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//        val view = binding.root
//        firebaseDatabase = FirebaseDatabase.getInstance()
//        databaseReference = firebaseDatabase.reference.child("users")
//        firebaseAuth = FirebaseAuth.getInstance()  // Inițializează FirebaseAuth
//        setListeners()
//        return view
//    }
//
//    private fun setListeners() {
//        binding.loginButton.setOnClickListener {
//            if (areFieldsEmpty()) {
//                showSnackbar(requireView(), getString(R.string.message_username_password_empty))
//            } else if (!NetworkUtils.isNetworkAvailable(requireContext())) {
//                showSnackbar(requireView(), getString(R.string.message_no_network))
//            } else {
//                val email = binding.emailInputField.text.toString()
//                val password = binding.passwordInputField.text.toString()
//
//                // Folosește Firebase Authentication pentru autentificare
//                firebaseAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            // Login reușit
//                            val currentUser = firebaseAuth.currentUser
//                            if (currentUser != null) {
//                                val userId = currentUser.uid
//                                fetchUserData(userId)
//                                showSnackbar(requireView(), getString(R.string.message_login_successful))
//                                Navigator.getInstance().openHomeMoviesFragment()
//                            }
//                        } else {
//                            // Eroare la login
//                            showSnackbar(requireView(), getString(R.string.message_login_failed))
//                        }
//                    }
//            }
//        }
//
//        binding.textViewDontHaveAccount.setOnClickListener{
//            Navigator.getInstance().openRegisterFragment()
//        }
//
//        binding.textViewForgotPassword.setOnClickListener{
//            Navigator.getInstance().openForgotPasswordFragment()
//        }
//    }
//
//    private fun fetchUserData(userId: String) {
//        databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val userData = dataSnapshot.getValue(UserData::class.java)
//                if (userData != null) {
//                    saveCurrentUser(userData)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Toast.makeText(requireContext(), "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun saveCurrentUser(userData: UserData) {
//        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("userId", userData.id)
//        editor.putString("username", userData.username)
//        editor.putString("email", userData.email)
//        editor.apply()
//    }
//
//    private fun showSnackbar(view: View, message: String) {
//        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
//    }
//
//    private fun areFieldsEmpty(): Boolean {
//        val usernameInputField = binding.usernameInputField
//        val passwordInputField = binding.passwordInputField
//        val emailInputField = binding.emailInputField
//
//        if (usernameInputField.text.toString().isEmpty()) {
//            usernameInputField.error = getString(R.string.invalid_username)
//            return true
//        }
//
//        if (passwordInputField.text.toString().isEmpty()) {
//            passwordInputField.error = getString(R.string.invalid_password)
//            return true
//        }
//
//        if (emailInputField.text.toString().isEmpty()) {
//            emailInputField.error = getString(R.string.invalid_email)
//            return true
//        }
//        return false
//    }
//}
