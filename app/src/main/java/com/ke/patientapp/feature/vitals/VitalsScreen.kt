package com.ke.patientapp.feature.vitals

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ke.patientapp.feature.common.DateField
import kotlinx.coroutines.flow.collectLatest


@Composable
fun VitalsScreen(
    modifier: Modifier = Modifier,
    nav: NavHostController,
    viewModel: VitalsViewModel = hiltViewModel()
) {
    val ui by viewModel.uiState.collectAsState()
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

    LaunchedEffect(ui) {
        viewModel.next.collectLatest { dest ->
            if (dest != null) {
                nav.navigate(dest)
            }
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = ui.patientName,
            onValueChange = {},
            label = { Text("Full name") },
            supportingText = { Text(ui.patientName) },
            isError = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        DateField(value = ui.visitDate, label = "Visit date", onPick = viewModel::onVisitDateChange, error = ui.invalidVisitDateError,enabled = true)


        OutlinedTextField(
            value = ui.height,
            onValueChange = viewModel::onPatientHeightChange,
            label = { Text("Height (cm)") },
            supportingText = { ui.invalidHeightError?.let { Text(it) } },
            isError = ui.invalidHeightError != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = ui.weight,
            onValueChange = viewModel::onPatientWeightChange,
            label = { Text("Weight (kg)") },
            supportingText = { ui.invalidWeightError?.let { Text(it) } },
            isError = ui.invalidWeightError != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )


        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AnimatedContent(
                    targetState = ui.bmi,
                    transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                    label = "BmiAnimatedContent"
                ) { bmiText ->
                    Column {
                        Text(text = if (bmiText.isNotBlank()) "BMI: $bmiText" else "BMI: —")

                        Spacer(Modifier.height(8.dp))


                        Text(text = if (ui.bmi.isNotBlank()) "Status: ${ui.bmi.determineBmiStatus()}" else "Status: —")

                    }
                }
            }
        }


        Button(
            onClick = { viewModel.onSubmit() },
            enabled = ui.canSubmit && !ui.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (ui.loading) {
                CircularProgressIndicator(
                    Modifier.size(20.dp),
                    strokeWidth = 2.dp
                ); Spacer(Modifier.width(8.dp))
            }
            Text("Save & Continue")
        }
    }
}