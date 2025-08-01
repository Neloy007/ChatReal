package com.example.chatreal

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chatreal.databinding.ItemChatMessageBinding

class ChatAdapter(
    private val messages: MutableList<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        val context = holder.binding.root.context
        val layoutParams = holder.binding.messageCard.layoutParams as ViewGroup.MarginLayoutParams

        holder.binding.messageTextView.text = message.text

        // Determine if message is sent or received based on senderId
        val isSentByMe = message.senderId == currentUserId

        if (isSentByMe) {
            holder.binding.messageCard.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.sentMessageBackground)
            )
            layoutParams.marginStart = 80
            layoutParams.marginEnd = 16
            holder.binding.messageTextView.gravity = Gravity.END
        } else {
            holder.binding.messageCard.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.receivedMessageBackground)
            )
            layoutParams.marginStart = 16
            layoutParams.marginEnd = 80
            holder.binding.messageTextView.gravity = Gravity.START
        }

        holder.binding.messageCard.layoutParams = layoutParams
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun setMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}
