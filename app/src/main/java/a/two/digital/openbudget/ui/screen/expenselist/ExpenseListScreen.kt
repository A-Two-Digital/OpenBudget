package a.two.digital.openbudget.ui.screen.expenselist

import a.two.digital.openbudget.R
import a.two.digital.openbudget.logic.ExpenseListViewModel
import a.two.digital.openbudget.ui.screen.expenselist.component.DateButton
import a.two.digital.openbudget.ui.screen.expenselist.component.ExpenseFAB
import a.two.digital.openbudget.ui.screen.expenselist.component.RoundButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun ExpenseScreen(expenseListViewModel: ExpenseListViewModel) {

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val items by expenseListViewModel.getExpensesForDate(selectedDate.toEpochDay())
        .collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { ExpenseFAB(selectedDate) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 10.dp)
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

                items.forEach { (expense, items) ->
                    Text(expense.title)
                }

            }

        }
    }
}