package com.example.fitnessfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Send user to MainActivity as soon as this activity loads
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // remove this activity from the stack
        finish()
    }
}