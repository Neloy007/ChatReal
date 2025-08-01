package com.example.chatreal



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatreal.databinding.ItemChatMessageBinding

class ChatAdapter(
    private val messages: MutableList<Message>
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.binding.messageTextView.text = message.text

        // Align message bubble left/right based on sender
        val params = holder.binding.messageCard.layoutParams as ViewGroup.MarginLayoutParams
        if (message.isSentByMe) {
            holder.binding.messageCard.setCardBackgroundColor(holder.binding.root.context.getColor(R.color.sentMessageBackground))
            params.marginStart = 80
            params.marginEnd = 16
            holder.binding.messageCard.layoutParams = params
            holder.binding.messageTextView.textAlignment = android.view.View.TEXT_ALIGNMENT_TEXT_END
        } else {
            holder.binding.messageCard.setCardBackgroundColor(holder.binding.root.context.getColor(R.color.receivedMessageBackground))
            params.marginStart = 16
            params.marginEnd = 80
            holder.binding.messageCard.layoutParams = params
            holder.binding.messageTextView.textAlignment = android.view.View.TEXT_ALIGNMENT_TEXT_START
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
