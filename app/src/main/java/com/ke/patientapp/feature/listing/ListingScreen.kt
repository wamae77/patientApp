package com.ke.patientapp.feature.listing

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
    } catch (_: Exception) { null }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = stringToMillis(filterDateStr)
            ?: LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    Box(
        modifier = modifier.fillMaxSize().padding(16.dp),
    ) {
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
                            Icon(Icons.Outlined.DateRange, contentDescription = "Filter by date")
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
                    PatientRow(row) {onItemClick(row.patientDbId) }
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
    onClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick(row.patientDbId) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = row.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B2B2B)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Age: ${row.age}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            if (row.lastVisitDate != null) {
                Text(
                    text = "Last Visit: ${row.lastVisitDate}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            val statusColor = when (row.lastBmiStatus?.lowercase()) {
                "underweight" -> Color(0xFF1E88E5)
                "normal" -> Color(0xFF43A047)
                "overweight" -> Color(0xFFE53935)
                else -> Color.Gray
            }

            Text(
                text = row.lastBmi ?: "--",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = statusColor
            )
            row.lastBmiStatus?.let {
                Text(
                    text = it,
                    fontSize = 13.sp,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }
    }
}
