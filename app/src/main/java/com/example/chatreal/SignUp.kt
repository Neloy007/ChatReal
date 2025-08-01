package com.example.chatreal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.chatreal.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var selectedImageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.imageProfile.setImageURI(it)
        }
    }

    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.imageProfile.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.signUpButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.ConfirmPasswordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create user in Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        // Upload profile image if selected
                        if (selectedImageUri != null) {
                            uploadProfileImageAndSaveUser(firebaseUser.uid, name, email, selectedImageUri!!)
                        } else {
                            // No image selected, save user data with empty image url
                            saveUserToFirestore(firebaseUser.uid, name, email, "")
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Signup failed: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        binding.alreadyHaveAccountTv.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }
    }

    private fun uploadProfileImageAndSaveUser(uid: String, name: String, email: String, imageUri: Uri) {
        val storageRef = storage.reference.child("profile_images/$uid.jpg")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveUserToFirestore(uid, name, email, downloadUri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveUserToFirestore(uid: String, name: String, email: String, profileImageUrl: String) {
        val userMap = mapOf(
            "name" to name,
            "email" to email,
            "profileImageUrl" to profileImageUrl
        )

        firestore.collection("users")
            .document(uid)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                // Navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("email", email)
                    putExtra("profileImageUrl", profileImageUrl)
                }
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save user data: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}
