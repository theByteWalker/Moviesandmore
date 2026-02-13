package com.example.moviesandmore

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviesandmore.presentation.login.LoginScreen
import com.example.moviesandmore.ui.theme.MoviesAndMoreTheme
import com.example.moviesandmore.utils.SecurityUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesAndMoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var loginError by remember { mutableStateOf<String?>(null) }

                    LoginScreen(
                        onLoginClick = { username, password ->
                            if (username == "admin" && password == "password") {
                                loginError = null
                                saveCredentials(username, password)
                                navigateToMainActivity()
                            } else {
                                loginError = "Invalid username or password"
                            }
                        },
                        error = loginError
                    )
                }
            }
        }
    }

    private fun saveCredentials(username: String, password: String) {
        val sharedPref = getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", SecurityUtils.encode(username))
            putString("password", SecurityUtils.encode(password))
            apply()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
