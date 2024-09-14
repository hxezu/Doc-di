package com.example.doc_di.reminder.medicationconfirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.doc_di.R
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.reminder.medicationconfirm.viewmodel.MedicationConfirmState
import com.example.doc_di.reminder.medicationconfirm.viewmodel.MedicationConfirmViewModel
import com.example.doc_di.util.SnackbarUtil.Companion.showSnackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Composable
fun MedicationConfirmRoute(
    reminder: List<Reminder>?,
    onBackClicked: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MedicationConfirmViewModel = hiltViewModel()
) {
    reminder?.let {
        MedicationConfirmScreen(
            reminders = it,
            viewModel = viewModel,
            onBackClicked = onBackClicked,
            navigateToHome = navigateToHome,
            logEvent = viewModel::logEvent
        )
    } ?: {
        FirebaseCrashlytics.getInstance().log("Error: Cannot show MedicationConfirmScreen. Medication is null.")
    }
}

@Composable
fun MedicationConfirmScreen(
    reminders: List<Reminder>,
    viewModel: MedicationConfirmViewModel,
    logEvent: (String) -> Unit,
    onBackClicked: () -> Unit,
    navigateToHome: () -> Unit,
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel
            .isMedicationSaved
            .collect {
                showSnackbar(
                    context.getString(
                        R.string.medication_timely_reminders_setup_message,
                        reminders.first().name
                    )
                )
                navigateToHome()
                logEvent.invoke(AnalyticsEvents.MEDICATIONS_SAVED)
            }
    }

    Column(
        modifier = Modifier.padding(0.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingActionButton(
            onClick = {
                logEvent.invoke(AnalyticsEvents.MEDICATION_CONFIRM_ON_BACK_CLICKED)
                onBackClicked()
            },
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "All done!",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )

        val medication = reminders.first()
        Text(
            text = pluralStringResource(
                id = R.plurals.all_set,
                count = reminders.size,
                medication.name,
                reminders.size,
                medication.recurrence.lowercase(),
                medication.endDate.toFormattedDateString()
            ),
            style = MaterialTheme.typography.titleMedium
        )
    }

    Column(
        modifier = Modifier
            .padding(0.dp, 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                logEvent.invoke(AnalyticsEvents.MEDICATION_CONFIRM_ON_CONFIRM_CLICKED)
                viewModel.addMedication(context, MedicationConfirmState(reminders))
            },
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = "Confirm",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}