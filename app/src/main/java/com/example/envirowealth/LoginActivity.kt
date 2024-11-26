package com.example.envirowealth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Login Button logic
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            // Perform login logic (e.g., validate credentials)

            // Save login state in SharedPreferences
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", true)
            editor.apply()

            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Navigate to SignupActivity when clicking on the text
        val signupLink = findViewById<TextView>(R.id.signupRedirectText)
        signupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
