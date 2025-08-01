package com.example.chatreal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.chatreal.databinding.ActivitySignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var selectedImageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.imageProfile.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageProfile.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.signUpButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.ConfirmPasswordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                name = name,
                email = email,
                password = password,
                profileImageUri = selectedImageUri?.toString() ?: ""
            )

            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getInstance(this@SignUp)
                db.userDao().insertUser(user)

                runOnUiThread {
                    val intent = Intent(this@SignUp, MainActivity::class.java).apply {
                        putExtra("name", user.name)
                        putExtra("email", user.email)
                        putExtra("imageUri", user.profileImageUri)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.alreadyHaveAccountTv.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }
    }
}
