package com.example.envirowealth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val emailField = findViewById<EditText>(R.id.signup_email)
        val passwordField = findViewById<EditText>(R.id.signup_password)
        val confirmPasswordField = findViewById<EditText>(R.id.signup_confirm)
        val signupButton = findViewById<Button>(R.id.signup_button)
        val loginLink = findViewById<TextView>(R.id.loginRedirectText)

        // Signup Button logic
        signupButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new user with FirebaseAuth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Navigate to LoginActivity
                        Toast.makeText(this, "Signup Successful! Please login.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        // Handle signup failure
                        Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Navigate to LoginActivity
        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
