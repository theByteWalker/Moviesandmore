package com.example.moviesandmore.presentation.ui.components
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviesandmore.R
import com.example.moviesandmore.presentation.Routes
import androidx.core.content.edit

@Composable
fun Login(padding: PaddingValues, navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("User") }
    val isLoginEnabled = username.isNotBlank() && password.isNotBlank()

    Column(
        modifier = Modifier.padding(padding).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_svgrepo_com),
                    contentDescription = "Login Logo",
                    modifier = Modifier.size(40.dp)
                )
                GreetingMessage(stringResource(id = R.string.app_name))
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = "dummy_username",
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "password",
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Password") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedRole == "User", onClick = { selectedRole = "User" })
                    Text("User")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedRole == "Admin", onClick = { selectedRole = "Admin" })
                    Text("Admin")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                Log.d("LoginData",
                    "User: $username, Pass: $password, Role: $selectedRole")
                val sharedPref = context.getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
                sharedPref.edit { putString("username", "dummy_username") }
                navController.navigate(Routes.POPULAR) {
                    popUpTo("login") { inclusive = true }
                }
            }, enabled = true, modifier = Modifier.testTag("login_btn") ) {
                Text("Login")
            }
        }
    }
}