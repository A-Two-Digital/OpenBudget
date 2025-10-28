package a.two.digital.openbudget.ui

import a.two.digital.openbudget.R
import a.two.digital.openbudget.ui.theme.OpenBudgetTheme
import a.two.digital.openbudget.ui.theme.Purple40
import a.two.digital.openbudget.ui.theme.Purple80
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenBudgetTheme {
                var selectedDate by remember { mutableStateOf(LocalDate.now()) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = { ExpenseFAB(LocalContext.current) },
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        //region Date section
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RoundButton(
                                onClick = { selectedDate = selectedDate.minusDays(1) },
                                icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = stringResource(R.string.previous_day)
                            )
                            DateButton(
                                selectedDate,
                                onDateSelected = { newDate -> selectedDate = newDate }
                            )
                            RoundButton(
                                onClick = { selectedDate = selectedDate.plusDays(1) },
                                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = stringResource(R.string.next_day)
                            )
                        }
                        //endregion
                    }
                }
            }
        }
    }
}

//region Date components
@Composable
fun RoundButton(onClick: () -> Unit, icon: ImageVector, contentDescription: String) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(icon, contentDescription = contentDescription, Modifier.size(25.dp))
    }
}

@Composable
fun DateButton(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    var showModal by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { showModal = true },
        modifier = Modifier.size(140.dp, 50.dp)
    ) {
        Text(text = selectedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
    }

    if (showModal) {
        DatePickerModal(
            onDateSelected = { dateInMillis ->
                dateInMillis?.let {
                    val newDate =
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(newDate)
                }
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun DatePickerModal(onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
//endregion

@Composable
fun ExpenseFAB(context: Context) {
    FloatingActionButton(
        onClick = { context.startActivity(Intent(context, AddExpenseActivity::class.java)) },
        containerColor = Purple40,
        contentColor = Purple80
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_an_expense)
        )
    }
}