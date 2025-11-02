package com.ke.patientapp.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onSignupClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") })
        Spacer(Modifier.height(16.dp))
        Button(onClick = {

        }, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }
        TextButton(onClick = onSignupClick, modifier = Modifier.fillMaxWidth()) {
            Text("Don't have an account? Sign up")
        }
    }
}
