package com.example.chatreal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatreal.databinding.ActivityChatBinding

class Chat : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName") ?: "Unknown"
        val userEmail = intent.getStringExtra("userEmail") ?: "No Email"
        val userImageRes = intent.getIntExtra("userImageResId", R.drawable.profileplus)

        binding.chatUserName.text = userName
        binding.chatUserImage.setImageResource(userImageRes)

        binding.chatUserImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("userName", userName)
                putExtra("userEmail", userEmail)
                putExtra("userImageResId", userImageRes)
            }
            startActivity(intent)
        }


        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.adapter = chatAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)


        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val message = Message(text = messageText, isSentByMe = true)
                chatAdapter.addMessage(message)
                binding.messageEditText.text?.clear()
                binding.chatRecyclerView.scrollToPosition(messages.size - 1)
            } else {
                Toast.makeText(this, "Cannot send empty message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
