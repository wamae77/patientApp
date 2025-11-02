package com.ke.patientapp.feature.listing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ke.patientapp.feature.listing.state.UiRow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun ListingScreen(
    modifier: Modifier = Modifier,
    onRegistrationClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    viewModel: ListingViewModel = hiltViewModel(),
) {
    val lazyItems = viewModel.paged.collectAsLazyPagingItems()
    val filterDateStr by viewModel.filterDate.collectAsState()
    val formatter = remember { DateTimeFormatter.ISO_LOCAL_DATE }
    var showDateDialog by remember { mutableStateOf(false) }

    fun stringToMillis(dateStr: String?): Long? = try {
        dateStr?.let {
            LocalDate.parse(it, formatter)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }
    } catch (_: Exception) {
        null
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = stringToMillis(filterDateStr)
            ?: LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {

        if (lazyItems.itemCount == 0) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Patients", style = MaterialTheme.typography.titleLarge)
                        filterDateStr?.let {
                            Spacer(Modifier.height(4.dp))
                            AssistChip(
                                onClick = { showDateDialog = true },
                                label = { Text("Date: $it") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { showDateDialog = true }) {
                            Icon(
                                Icons.Outlined.DateRange,
                                contentDescription = "Filter by date"
                            )
                        }
                        if (filterDateStr != null) {
                            IconButton(onClick = { viewModel.setFilterDate(null) }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear date")
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = "Empty list",
                        modifier = Modifier.size(128.dp)
                    )
                }
            }

        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Patients", style = MaterialTheme.typography.titleLarge)
                            filterDateStr?.let {
                                Spacer(Modifier.height(4.dp))
                                AssistChip(
                                    onClick = { showDateDialog = true },
                                    label = { Text("Date: $it") },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear"
                                        )
                                    }
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { showDateDialog = true }) {
                                Icon(
                                    Icons.Outlined.DateRange,
                                    contentDescription = "Filter by date"
                                )
                            }
                            if (filterDateStr != null) {
                                IconButton(onClick = { viewModel.setFilterDate(null) }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear date")
                                }
                            }
                        }
                    }
                }

                items(
                    count = lazyItems.itemCount,
                    key = lazyItems.itemKey { it.patientDbId },
                    contentType = lazyItems.itemContentType()
                ) { index ->
                    lazyItems[index]?.let { row ->
                        PatientRow(row = row, onClick = { onItemClick(row.patientDbId) })
                    }
                }

                item {
                    when (val s = lazyItems.loadState.append) {
                        is LoadState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
                        is LoadState.Error -> Text("Error loading more: ${s.error.message}")
                        else -> {}
                    }
                }
            }
        }
        when (val s = lazyItems.loadState.refresh) {
            is LoadState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            is LoadState.Error -> Text("Error: ${s.error.message}")
            else -> {}
        }

        ExtendedFloatingActionButton(
            onClick = onRegistrationClick,
            icon = { Icon(Icons.Filled.PersonAdd, contentDescription = null) },
            text = { Text("Add Patient") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding()
                .imePadding()
        )
    }

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    val pickedStr = millis?.let {
                        Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(formatter)
                    }
                    viewModel.setFilterDate(pickedStr)
                    showDateDialog = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDateDialog = false }) { Text("Cancel") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Composable
fun PatientRow(
    row: UiRow,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(row.patientDbId) }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = row.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Age: ${row.age}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            row.lastVisitDate?.let {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Last Visit: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = row.lastBmi ?: "--",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            row.lastBmiStatus?.let { status ->
                Spacer(Modifier.height(4.dp))
                StatusChip(label = status)
            }
        }
    }
}

@Composable
private fun StatusChip(label: String, modifier: Modifier = Modifier) {
    val tint = when (label.lowercase()) {
        "underweight" -> Color(0xFF22D3EE)
        "normal" -> Color(0xFF34D399)
        "overweight" -> Color(0xFFF59E0B)
        "obese" -> Color(0xFFF43F5E)
        else -> MaterialTheme.colorScheme.outline
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(tint.copy(alpha = 0.12f))
            .border(BorderStroke(1.dp, tint.copy(alpha = 0.6f)), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = tint
        )
    }
}
