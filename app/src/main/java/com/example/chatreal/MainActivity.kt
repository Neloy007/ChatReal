package com.example.chatreal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chatreal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser
        if (user != null) {
            binding.nameTextView.text = user.displayName ?: "No Name"
            binding.emailTextView.text = user.email ?: "No Email"

            if (user.photoUrl != null) {
                Glide.with(this)
                    .load(user.photoUrl)
                    .placeholder(R.drawable.profileplus)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.profileplus)
            }

            Toast.makeText(this, "Welcome, ${user.displayName ?: "User"}!", Toast.LENGTH_SHORT).show()
        } else {
            // No user signed in, redirect to SignIn
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
