package com.example.envirowealth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Signup Button logic (add your signup logic here)
        val signupButton = findViewById<Button>(R.id.signup_button)
        signupButton.setOnClickListener {
            // Perform signup logic (e.g., save user info)
            // Navigate to LoginActivity after signup
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Navigate to LoginActivity
        val loginLink = findViewById<TextView>(R.id.loginRedirectText)
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
