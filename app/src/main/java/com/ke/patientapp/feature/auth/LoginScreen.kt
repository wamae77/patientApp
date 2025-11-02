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
fun LoginScreen(
    onSignupClick: () -> Unit,
    onLoggedIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }

                LoginEffect.NavigateHome -> onLoggedIn()
                LoginEffect.NavigateLogin -> {}
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            isError = state.emailError != null,
            supportingText = {
                if (state.emailError != null) Text(state.emailError!!)
            },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            isError = state.passwordError != null,
            supportingText = {
                if (state.passwordError != null) Text(state.passwordError!!)
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = viewModel::onLoginClick,
            enabled = state.isLoginEnabled && !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Signing in…")
            } else {
                Text("Login")
            }
        }

        TextButton(onClick = onSignupClick, modifier = Modifier.fillMaxWidth()) {
            Text("Don't have an account? Sign up")
        }
    }
}

