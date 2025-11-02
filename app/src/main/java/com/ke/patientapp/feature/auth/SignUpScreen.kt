package com.ke.patientapp.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel


@Composable
fun SignupScreen(onLoginClick: () -> Unit,viewModel: AuthViewModel= hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("FirstName") })
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("LastName") })
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(Modifier.height(16.dp))
        Button(onClick = {  }, modifier = Modifier.fillMaxWidth()) {
            Text("Sign Up")
        }
        TextButton(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
            Text("Already have an account? Login")
        }
    }
}
