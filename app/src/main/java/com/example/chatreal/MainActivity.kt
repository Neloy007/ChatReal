package com.example.chatreal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatreal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter

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
            startActivity(Intent(this, SignIn::class.java))
            finish()
            return
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        userList = ArrayList()
        adapter = UserAdapter(userList) { clickedUser ->
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("chatPartnerId", clickedUser.id)
                putExtra("userName", clickedUser.name)
                putExtra("userEmail", clickedUser.email)
                putExtra("userImageUri", clickedUser.profileImageUrl)
            }
            startActivity(intent)
        }

        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.adapter = adapter

        loadAllUsers()
    }

    private fun loadAllUsers() {
        val currentUserId = auth.currentUser?.uid ?: return

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                userList.clear()
                for (document in result) {
                    val user = document.toObject(User::class.java).copy(id = document.id)
                    if (user.id != currentUserId) {
                        userList.add(user)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load users: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
