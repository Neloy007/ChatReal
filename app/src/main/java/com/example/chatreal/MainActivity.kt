package com.example.chatreal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatreal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get passed data
        val name = intent.getStringExtra("name") ?: "User"
        val email = intent.getStringExtra("email") ?: "No Email"

        // Set text
        binding.nameTextView.text = "Name: $name"
        binding.emailTextView.text = "Email: $email"

        Toast.makeText(this, "Welcome, $name!", Toast.LENGTH_SHORT).show()


        binding.logoutButton.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }
}
