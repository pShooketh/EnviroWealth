package com.example.envirowealth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is logged in using shared preferences
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // User is logged in, navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            // Finish LauncherActivity to prevent going back to it
            finish()
        } else {
            // User is not logged in, show launcher screen with login/signup options
            setContentView(R.layout.activity_launcher)

            // Button to navigate to LoginActivity
            val loginButton = findViewById<Button>(R.id.loginButton)
            loginButton.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            // Button to navigate to SignupActivity
            val signupButton = findViewById<Button>(R.id.signupButton)
            signupButton.setOnClickListener {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
