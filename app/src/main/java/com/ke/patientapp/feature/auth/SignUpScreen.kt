package com.ke.patientapp.feature.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.patientapp.feature.auth.state.LoginEffect


@Composable
fun SignupScreen(
    onLoginClick: () -> Unit,
    onSignupSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.signupState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                LoginEffect.NavigateHome -> onSignupSuccess()
                LoginEffect.NavigateLogin -> onLoginClick()
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onSignupEmailChange,
            label = { Text("Email") },
            isError = state.emailError != null,
            supportingText = { if (state.emailError != null) Text(state.emailError!!) }
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.firstName,
            onValueChange = viewModel::onFirstNameChange,
            label = { Text("First Name") },
            isError = state.firstNameError != null,
            supportingText = { if (state.firstNameError != null) Text(state.firstNameError!!) }
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.lastName,
            onValueChange = viewModel::onLastNameChange,
            label = { Text("Last Name") },
            isError = state.lastNameError != null,
            supportingText = { if (state.lastNameError != null) Text(state.lastNameError!!) }
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onSignupPasswordChange,
            label = { Text("Password") },
            isError = state.passwordError != null,
            supportingText = { if (state.passwordError != null) Text(state.passwordError!!) },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = viewModel::onSignupClick,
            enabled = state.isSignupEnabled && !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Creating account…")
            } else {
                Text("Sign Up")
            }
        }

        TextButton(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
            Text("Already have an account? Login")
        }
    }
}
