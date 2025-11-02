package com.ke.patientapp.feature.listing

import android.R.attr.onClick
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


@Composable
fun ListingScreen(
    modifier: Modifier = Modifier,
    onRegistrationClick:()->Unit,
    viewModel: ListingViewModel = hiltViewModel(),
) {
    val lazyItems = viewModel.paged.collectAsLazyPagingItems()
    val filterDate by viewModel.filterDate.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        //     verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                //.weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {

            }

            items(
                count = lazyItems.itemCount,
                key = lazyItems.itemKey { it.patientDbId },
                contentType = lazyItems.itemContentType()
            ) { index ->
                lazyItems[index]?.let { row ->
                    PatientRow(row) { /* onOpenPatient(row.patientDbId) */ }
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
