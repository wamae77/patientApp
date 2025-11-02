package com.ke.patientapp.feature.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ke.patientapp.feature.common.DateField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegistrationScreen(
    onSaved: (patientDbId: Long) -> Unit = {},
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val ui by viewModel.uiState.collectAsState()
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collectLatest { id->
            if (id != null) {
                onSaved(id)
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = ui.patientId,
            onValueChange = viewModel::onPatientIdChange,
            label = { Text("Patient ID") },
            supportingText = { ui.patientIdError?.let { Text(it) } },
            isError = ui.patientIdError != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        DateField(
            value = ui.registrationDate,
            label = "Registration date",
            onPick = viewModel::onRegistrationDateChange
        )


        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = ui.firstName,
                onValueChange = viewModel::onFirstNameChange,
                label = { Text("First name") },
                supportingText = { ui.firstNameError?.let { Text(it) } },
                isError = ui.firstNameError != null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = ui.lastName,
                onValueChange = viewModel::onLastNameChange,
                label = { Text("Last name") },
                supportingText = { ui.lastNameError?.let { Text(it) } },
                isError = ui.lastNameError != null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        DateField(
            value = ui.dateOfBirth,
            label = "Date of birth",
            onPick = viewModel::onDateOfBirthChange
        )


        GenderDropdown(
            selected = ui.gender,
            onSelected = viewModel::onGenderChange,
            error = ui.genderError
        )


        if (ui.generalError != null) {
            AssistChip(onClick = {}, label = { Text(ui.generalError!!) }, leadingIcon = {
                Icon(Icons.Default.Error, contentDescription = null)
            })
        }


        Button(
            onClick = {
                focus.clearFocus(); viewModel.onSubmit()
            },
            enabled = ui.canSubmit && !ui.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (ui.loading) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
            }
            Text("Save & Continue")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdown(selected: String, onSelected: (String) -> Unit, error: String?) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female", "Other")
    Column {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                isError = error != null,
                supportingText = { error?.let { Text(it) } },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { opt ->
                    DropdownMenuItem(text = { Text(opt) }, onClick = {
                        onSelected(opt); expanded = false
                    })
                }
            }
        }
    }
}