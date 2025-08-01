package com.example.chatreal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatreal.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alreadyHaveAccountTv.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.ConfirmPasswordEditText.text.toString().trim()


            if (name.isEmpty()) {
                binding.nameInputLayout.error = "Name is required"
                return@setOnClickListener
            } else {
                binding.nameInputLayout.error = null
            }


            if (email.isEmpty()) {
                binding.emailInputLayout.error = "Email is required"
                return@setOnClickListener
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailInputLayout.error = "Invalid email address"
                return@setOnClickListener
            } else {
                binding.emailInputLayout.error = null
            }


            val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!_*?]).{6,}$")
            if (password.isEmpty()) {
                binding.passwordInputLayout.error = "Password is required"
                return@setOnClickListener
            } else if (!passwordPattern.matches(password)) {
                binding.passwordInputLayout.error =
                    "Password must contain uppercase, lowercase, number & special character"
                return@setOnClickListener
            } else {
                binding.passwordInputLayout.error = null
            }


            if (confirmPassword.isEmpty()) {
                binding.confirmPasswordInputLayout.error = "Please confirm your password"
                return@setOnClickListener
            } else if (confirmPassword != password) {
                binding.confirmPasswordInputLayout.error = "Passwords do not match"
                return@setOnClickListener
            } else {
                binding.confirmPasswordInputLayout.error = null
            }


            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()


            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("email", email)
            startActivity(intent)
            finish()
        }


    }
}