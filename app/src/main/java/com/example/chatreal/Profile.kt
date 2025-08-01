package com.example.chatreal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatreal.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("userName") ?: "User"
        val email = intent.getStringExtra("userEmail") ?: "No Email"
        val imageRes = intent.getIntExtra("userImageResId", R.drawable.profileplus)

        binding.profileName.text = name
        binding.profileEmail.text = email
        binding.profileImage.setImageResource(imageRes)
    }
}