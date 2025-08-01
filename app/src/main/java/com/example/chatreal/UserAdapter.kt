package com.example.chatreal


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatreal.databinding.ItemUserBinding

class UserAdapter(
    private val users: List<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.binding.nameTextView.text = user.name
        holder.binding.emailTextView.text = user.email

        if (!user.profileImageUri.isNullOrEmpty()) {
            holder.binding.profileImage.setImageURI(Uri.parse(user.profileImageUri))
        } else {
            holder.binding.profileImage.setImageResource(R.drawable.profileplus) // fallback image
        }

        holder.binding.root.setOnClickListener {
            onUserClick(user)
        }
    }


    override fun getItemCount(): Int = users.size
}

