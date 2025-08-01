package com.example.chatreal

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatreal.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    private lateinit var currentUserId: String
    private lateinit var chatPartnerId: String
    private lateinit var conversationId: String

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get user info from intent
        val userName = intent.getStringExtra("userName") ?: "Unknown"
        val userImageUri = intent.getStringExtra("userImageUri")?.let { Uri.parse(it) }

        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        chatPartnerId = intent.getStringExtra("chatPartnerId") ?: ""

        if (currentUserId.isEmpty() || chatPartnerId.isEmpty()) {
            Toast.makeText(this, "Invalid user IDs for chat", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        conversationId = getConversationId(currentUserId, chatPartnerId)

        // Display chat partner info
        binding.chatUserName.text = userName
        if (userImageUri != null) {
            Glide.with(this)
                .load(userImageUri)
                .placeholder(R.drawable.profileplus)
                .into(binding.chatUserImage)
        } else {
            binding.chatUserImage.setImageResource(R.drawable.profileplus)
        }

        // Setup RecyclerView and Adapter
        chatAdapter = ChatAdapter(messages, currentUserId)
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }

        // Send message on button click
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    senderId = currentUserId,
                    text = messageText,
                    timestamp = System.currentTimeMillis()
                )
                sendMessage(message)
            } else {
                Toast.makeText(this, "Cannot send empty message", Toast.LENGTH_SHORT).show()
            }
        }

        // Start listening for incoming messages
        listenForMessages()
    }

    private fun getConversationId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) "${userId1}_${userId2}" else "${userId2}_${userId1}"
    }



    private fun sendMessage(message: Message) {
        binding.sendButton.isEnabled = false
        db.collection("chats")
            .document(conversationId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                binding.messageEditText.text?.clear()
                binding.sendButton.isEnabled = true
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to send message: ${it.message}", Toast.LENGTH_SHORT).show()
                binding.sendButton.isEnabled = true
            }
    }

    private fun listenForMessages() {
        db.collection("chats")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "Listen failed: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                snapshots?.let {
                    val newMessages = it.documents.mapNotNull { doc -> doc.toObject(Message::class.java) }
                    messages.clear()
                    messages.addAll(newMessages)
                    chatAdapter.notifyDataSetChanged()
                    binding.chatRecyclerView.scrollToPosition(messages.size - 1)
                }
            }
    }
}
