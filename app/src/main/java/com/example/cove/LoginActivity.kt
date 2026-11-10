package com.example.cove

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cove.SecurityManager

class LoginActivity : AppCompatActivity() {

    private lateinit var securityManager: SecurityManager
    private val enteredPin = StringBuilder()

    private lateinit var pinDisplay: TextView
    private lateinit var instructionText: TextView
    private lateinit var errorText: TextView

    private var isSettingNewPin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        securityManager = SecurityManager(this)
        pinDisplay = findViewById(R.id.pinDisplay)
        instructionText = findViewById(R.id.instructionText)
        errorText = findViewById(R.id.errorText)

        // First launch prompts to establish PIN
        if (!securityManager.isPinSet()) {
            isSettingNewPin = true
            instructionText.text
        }

        setupKeypad()
    }

    private fun setupKeypad() {
        val keypadGrid = findViewById<GridLayout>(R.id.keypadGrid)

        for (i in 0 until keypadGrid.childCount) {
            val view = keypadGrid.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener { handleKeyInput(view.tag.toString()) }
            }
        }
    }

    private fun handleKeyInput(tag: String) {
        errorText.visibility = View.INVISIBLE

        when (tag) {
            "clear" -> {
                enteredPin.clear()
            }
            "delete" -> {
                if (enteredPin.isNotEmpty()) {
                    enteredPin.deleteCharAt(enteredPin.length - 1)
                }
            }
            else -> {
                if (enteredPin.length < 4) {
                    enteredPin.append(tag)
                }
            }
        }

        updatePinDisplay()

        if (enteredPin.length == 4) {
            validatePin()
        }
    }

    private fun updatePinDisplay() {
        val dots = StringBuilder()
        for (i in 0 until 4) {
            if (i < enteredPin.length) {
                dots.append("●  ")
            } else {
                dots.append("○  ")
            }
        }
        pinDisplay.text = dots.toString().trim()
    }

    private fun validatePin() {
        val pin = enteredPin.toString()

        if (isSettingNewPin) {
            securityManager.savePin(pin)
            navigateToMain()
        } else {
            if (securityManager.verifyPin(pin)) {
                navigateToMain()
            } else {
                errorText.text
                errorText.visibility = View.VISIBLE
                enteredPin.clear()
                updatePinDisplay()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Closes LoginActivity from the back stack
    }
}

