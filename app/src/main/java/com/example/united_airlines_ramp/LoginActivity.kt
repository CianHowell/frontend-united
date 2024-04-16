package com.example.united_airlines_ramp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.united_airlines_ramp.api.AuthService
import com.example.united_airlines_ramp.models.LoginRequest
import com.example.united_airlines_ramp.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var employeeIDEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the views
        employeeIDEditText = findViewById(R.id.employeeID)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)

        setupPasswordVisibilityToggle()

        loginButton.setOnClickListener {
            val employeeID = employeeIDEditText.text.toString()
            val password = passwordEditText.text.toString()
            performLogin(employeeID, password)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordVisibilityToggle() {
        passwordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = passwordEditText.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (passwordEditText.right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility(passwordEditText)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }



    private fun togglePasswordVisibility(editText: EditText) {
        val selectionStart = editText.selectionStart
        val selectionEnd = editText.selectionEnd
        if (editText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0)
        } else {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0)
        }
        editText.setSelection(selectionStart, selectionEnd)
    }

    private fun performLogin(employeeID: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your-api-base-url.com/") // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authService = retrofit.create(AuthService::class.java)
        val loginRequest = LoginRequest(employeeID, password)

        authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    // Successful login, save token, navigate to next activity
                    Toast.makeText(this@LoginActivity, "Logged In.", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle login failure with an alert
                    Toast.makeText(this@LoginActivity, "Login Failed.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network failure or error
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

