package com.example.cove

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cove.SecurityManager

class MainActivity : AppCompatActivity() {

    private lateinit var securityManager: SecurityManager
    private lateinit var titleText: TextView
    private lateinit var lockButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        securityManager = SecurityManager(this)

        if (!securityManager.isPinSet()) {
            navigateToLogin()
            return
        }

        setContentView(R.layout.activity_main)

        titleText = findViewById(R.id.titleText)
        lockButton = findViewById(R.id.lockButton)

        titleText.text = getString(R.string.app_name)

        lockButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}