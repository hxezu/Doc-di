package com.example.doc_di.reminder.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.extension.hasPassed
import com.example.doc_di.reminder.history.viewmodel.HistoryState
import com.example.doc_di.reminder.history.viewmodel.HistoryViewModel
import com.example.doc_di.reminder.home.utils.MedicationCard
import com.example.doc_di.reminder.home.MedicationListItem

@Composable
fun HistoryRoute(
    navigateToMedicationDetail: (Reminder) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state = viewModel.state
    HistoryScreen(
        state = state,
        navigateToMedicationDetail = navigateToMedicationDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    navigateToMedicationDetail: (Reminder) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(top = 16.dp),
                title = {
                    Text(
                        text = "History",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
            )
        },
        bottomBar = { },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MedicationList(
                state = state,
                navigateToMedicationDetail = navigateToMedicationDetail
            )
        }
    }
}

@Composable
fun MedicationList(
    state: HistoryState,
    navigateToMedicationDetail: (Reminder) -> Unit
) {

    val filteredMedicationList = state.reminders.filter { it.medicationTime.hasPassed() }
    val sortedMedicationList: List<MedicationListItem> = filteredMedicationList.sortedBy { it.medicationTime }.map { MedicationListItem.MedicationItem(it) }

    when (sortedMedicationList.isEmpty()) {
        true -> EmptyView()
        false -> MedicationLazyColumn(sortedMedicationList, navigateToMedicationDetail)
    }
}

@Composable
fun MedicationLazyColumn(sortedMedicationList: List<MedicationListItem>, navigateToMedicationDetail: (Reminder) -> Unit) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = sortedMedicationList,
            itemContent = {
                when (it) {
                    is MedicationListItem.OverviewItem -> { }
                    is MedicationListItem.HeaderItem -> {
                        Text(
                            modifier = Modifier
                                .padding(4.dp, 12.dp, 8.dp, 0.dp)
                                .fillMaxWidth(),
                            text = it.headerText.uppercase(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    is MedicationListItem.MedicationItem -> {
//                        MedicationCard(
//                            reminder = it.reminder,
//                            navigateToMedicationDetail = { medication ->
//                                navigateToMedicationDetail(medication)
//                            }
//                        )
                    }
                }
            }
        )
    }
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineMedium,
            text = "No history yet",
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}