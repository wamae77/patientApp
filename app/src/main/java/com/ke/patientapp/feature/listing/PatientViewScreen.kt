package com.ke.patientapp.feature.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.patientapp.core.data.local.entities.AssessmentEntity
import com.ke.patientapp.core.data.local.entities.PatientFullRecord
import com.ke.patientapp.core.data.local.entities.VitalsEntity

@Composable
fun PatientViewScreen(
    viewModel: PatientPreview = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        UiState.Loading ->{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }
        UiState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("No data found.") }
        }
        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("Error: ${s.message}") }
        }
        is UiState.Success -> PatientContent(s.record)
    }
}

@Composable
private fun PatientContent(record: PatientFullRecord) {
    val patient = record.patient
    val latestVitals = record.vitals.maxByOrNull { it.visitDate }
    val assessments = record.assessments.sortedByDescending { it.visitDate }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PatientHeaderCard(
                name = "${patient.firstName} ${patient.lastName}",
                id = patient.patientId,
                dob = patient.dateOfBirth,
                gender = patient.gender
            )
        }

        item {
            VitalsCard(latestVitals)
        }

        item {
            Text(
                text = "Assessments",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (assessments.isEmpty()) {
            item { Text("No assessments yet.", color = MaterialTheme.colorScheme.outline) }
        } else {
            items(assessments) { a ->
                AssessmentRow(a)
            }
        }

    }
}


@Composable
private fun PatientHeaderCard(name: String, id: String, dob: String, gender: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(name, style = MaterialTheme.typography.titleLarge)
            Text("ID: $id", style = MaterialTheme.typography.bodyMedium)
            Text("DOB: $dob", style = MaterialTheme.typography.bodyMedium)
            Text("Gender: $gender", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun VitalsCard(v: VitalsEntity?) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Latest Vitals", style = MaterialTheme.typography.titleMedium)
            if (v == null) {
                Text("No vitals recorded.", color = MaterialTheme.colorScheme.outline)
            } else {
                Text("Visit: ${v.visitDate}")
                Text("Height: ${v.heightCm} cm")
                Text("Weight: ${v.weightKg} kg")
                Text("BMI: ${v.bmi}")
            }
        }
    }
}

@Composable
private fun AssessmentRow(a: AssessmentEntity) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Visit: ${a.visitDate}", style = MaterialTheme.typography.bodyLarge)
            Text("General health: ${a.generalHealth}", style = MaterialTheme.typography.bodyMedium)
            Text("Ever been on a Diet: ${a.everBeenOnADietToLooseWeight}", style = MaterialTheme.typography.bodyMedium)
            Text("Are you currently taking drugs: ${a.areYouCurrentlyTakingDrugs}", style = MaterialTheme.typography.bodyMedium)
            Text("Comments: ${a.comments}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}