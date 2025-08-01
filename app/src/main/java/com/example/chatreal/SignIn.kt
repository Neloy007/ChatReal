package com.example.chatreal

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatreal.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.createNewAccountTv.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if(email.isEmpty()){
                binding.emailInputLayout.error = "Email is required"
                return@setOnClickListener
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailInputLayout.error = "Invalid email"
                return@setOnClickListener
            }else{
                binding.emailInputLayout.error = null
            }


            val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!_*?]).{6,}$")
            if(password.isEmpty()){
                binding.passwordInputLayout.error = "Password is required"
                return@setOnClickListener
            }else if(password.length < 6){
                binding.passwordInputLayout.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }else if(!passwordPattern.matches(password)){
                binding.passwordInputLayout.error = "Password must contain at least one uppercase, lowercase, number & special char"
                return@setOnClickListener
            }else{
                binding.passwordInputLayout.error = null

            }

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()


        }

    }
}