package com.ke.patientapp.feature.assessment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ke.patientapp.core.data.local.entities.AssessmentType
import com.ke.patientapp.feature.common.DateField
import com.ke.patientapp.feature.common.MultiLineTextField
import com.ke.patientapp.ui.theme.PatientappTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AssessmentScreen(
    modifier: Modifier = Modifier,
    viewModel: AssessmentViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {

    val ui by viewModel.uiState.collectAsState()
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

    val generalHealthOptions = listOf("Good", "Poor")
    val loseWeightOptions = listOf("Yes", "No")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(generalHealthOptions[0]) }
    val (selectedOption2, onOptionSelected2) = remember { mutableStateOf(loseWeightOptions[0]) }

    LaunchedEffect(Unit) {
        viewModel.navigation.collectLatest {
            if (it) {
                onSaveClick()
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

        DateField(value = ui.visitDate, label = "Visit date", onPick = viewModel::onVisitDateChange)

        Text("General Health?")
        Spacer(modifier = Modifier.height(8.dp))
        RadioButtonSingleSelection(
            radioOptions = generalHealthOptions,
            onOptionSelected = onOptionSelected,
            selectedOption = selectedOption
        )


        ui.assessment?.let { assessment ->
            val quiz = when (assessment) {
                AssessmentType.GENERAL -> "Have you ever been on a diet to lose weight?"
                AssessmentType.OVERWEIGHT -> "Are you currently taking any drugs?"
            }

            Text(quiz)
            Spacer(modifier = Modifier.height(8.dp))
            RadioButtonSingleSelection(
                radioOptions = loseWeightOptions,
                onOptionSelected = onOptionSelected2,
                selectedOption = selectedOption2
            )
        }
        MultiLineTextField(
            value = ui.comments,
            onValueChange = viewModel::onCommentChange,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ActionButtons(
            onClose = { onBackClick() },
            onSave = { viewModel.saveAssessment(selectedOption, selectedOption2) }
        )
    }
}


@Composable
fun ActionButtons(
    onClose: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onClose,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text("Close")
        }

        Button(
            onClick = onSave
        ) {
            Text("Save")
        }
    }
}

@Preview
@Composable
fun PreviewRadioButtonSingleSelection() {
    PatientappTheme() {
        Column() {
            val radioOptions = listOf("One", "Two")
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
            RadioButtonSingleSelection(
                radioOptions = radioOptions,
                onOptionSelected = onOptionSelected,
                selectedOption = selectedOption
            )
        }
    }
}

@Composable
fun RadioButtonSingleSelection(
    modifier: Modifier = Modifier,
    radioOptions: List<String>,
    onOptionSelected: (String) -> Unit,
    selectedOption: String
) {

    Column(modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}