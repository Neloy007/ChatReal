package com.example.chatreal

import android.content.Intent
import android.net.Uri
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
        val userImageUriString = intent.getStringExtra("userImageUri")
        val userImageUri = if (!userImageUriString.isNullOrEmpty()) Uri.parse(userImageUriString) else null

        binding.chatUserName.text = userName
        if (userImageUri != null) {
            binding.chatUserImage.setImageURI(userImageUri)
        } else {
            binding.chatUserImage.setImageResource(R.drawable.profileplus)
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
