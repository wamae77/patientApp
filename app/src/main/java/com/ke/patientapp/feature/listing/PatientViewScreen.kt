package com.ke.patientapp.feature.listing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.patientapp.core.data.local.entities.AssessmentEntity
import com.ke.patientapp.core.data.local.entities.PatientFullRecord
import com.ke.patientapp.core.data.local.entities.SyncState
import com.ke.patientapp.core.data.local.entities.VitalsEntity

@Composable
fun PatientViewScreen(
    viewModel: PatientPreview = hiltViewModel(),
    onVitalsSelected: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        UiState.Loading -> {
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

        is UiState.Success -> PatientContent(
            s.record,
            onAddVitals = onVitalsSelected,
        )
    }
}

@Composable
private fun PatientContent(
    record: PatientFullRecord,
    onAddVitals: (Long) -> Unit = {},
) {
    val patient = record.patient
    val latestVitals = record.vitals.sortedByDescending { it.visitDate }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Vitals",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedButton(onClick = { onAddVitals(patient.id) }) {
                    Text("Add Vitals")
                }
            }
        }
        if (latestVitals.isEmpty()) {
            item { Text("No Vitals yet.", color = MaterialTheme.colorScheme.outline) }
        } else {
            items(latestVitals) { a ->
                VitalsCard(a)
            }
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
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Latest Vitals", style = MaterialTheme.typography.titleMedium)

            if (v == null) {
                Text("No vitals recorded.", color = MaterialTheme.colorScheme.outline)
            } else {
                SyncStatusLabel(
                    state = v.syncState,
                    modifier = Modifier.wrapContentWidth()
                )

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
            SyncStatusLabel(
                state = a.syncState,
                modifier = Modifier.wrapContentWidth()
            )
            Text("Visit: ${a.visitDate}", style = MaterialTheme.typography.bodyLarge)
            Text("General health: ${a.generalHealth}", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Ever been on a Diet: ${a.everBeenOnADietToLooseWeight}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Are you currently taking drugs: ${a.areYouCurrentlyTakingDrugs}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Comments: ${a.comments}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SyncStatusLabel(
    state: SyncState,
    modifier: Modifier = Modifier
) {
    val amber = Color(0xFFF59E0B)
    val cyan = Color(0xFF22D3EE)
    val green = Color(0xFF34D399)
    val rose = Color(0xFFF43F5E)

    val (label, tint) = when (state) {
        SyncState.PENDING -> "Sync pending" to amber
        SyncState.SYNCING -> "Syncing…" to cyan
        SyncState.SYNCED -> "Synced" to green
        SyncState.FAILED -> "Sync failed" to rose
    }

    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .clip(shape)
            .background(tint.copy(alpha = 0.12f))
            .border(BorderStroke(1.dp, tint.copy(alpha = 0.6f)), shape)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = tint,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

