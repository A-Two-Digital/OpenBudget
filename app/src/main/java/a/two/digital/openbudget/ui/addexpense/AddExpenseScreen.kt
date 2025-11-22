package a.two.digital.openbudget.ui.addexpense

import a.two.digital.openbudget.R
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import a.two.digital.openbudget.logic.ExpenseWithItemsViewModel
import a.two.digital.openbudget.ui.addexpense.components.AddExpenseButton
import a.two.digital.openbudget.ui.addexpense.components.AddExpenseTitle
import a.two.digital.openbudget.ui.addexpense.components.DetailedItemEditor
import a.two.digital.openbudget.ui.addexpense.components.SimpleItemEditor
import a.two.digital.openbudget.ui.components.ChoiceSwitch
import a.two.digital.openbudget.ui.components.DateTextField
import a.two.digital.openbudget.ui.components.TextField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun AddExpenseScreen(
    expenseWithItemsViewModel: ExpenseWithItemsViewModel,
    expenseTypeViewModel: ExpenseTypeViewModel,
    onClose: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val state by expenseWithItemsViewModel.expenseWithItems.collectAsState()
    val validation by expenseWithItemsViewModel.validationState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AddExpenseTitle { onClose() } }
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
                    .verticalScroll(rememberScrollState())
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { focusManager.clearFocus() }
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    R.string.title,
                    R.string.title_placeholder,
                    validation.isTitleError,
                    state.expense.title
                ) { expenseWithItemsViewModel.updateTitle(it) }
                DateTextField(
                    R.string.date,
                    state.expense.date
                ) { expenseWithItemsViewModel.updateDate(it) }

                var isDetailed by rememberSaveable { mutableStateOf(false) }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                ChoiceSwitch(
                    R.string.simple_or_detailed_label,
                    R.string.simple_or_detailed_description,
                    isDetailed
                ) { isDetailed = it }
                ChoiceSwitch(
                    R.string.spent_or_gained_label,
                    R.string.spent_or_gained_description,
                    state.expense.isIncoming
                ) { expenseWithItemsViewModel.updateIsIncoming(it) }
                ChoiceSwitch(
                    R.string.recurring_label,
                    R.string.recurring_description,
                    false
                ) {}
                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                if (!isDetailed) {
                    SimpleItemEditor(
                        state.items.first(),
                        validation,
                        expenseWithItemsViewModel,
                        expenseTypeViewModel
                    )
                } else {
                    DetailedItemEditor(
                        state.items,
                        validation,
                        expenseWithItemsViewModel,
                        expenseTypeViewModel
                    )
                }

                AddExpenseButton(
                    R.string.add,
                    { expenseWithItemsViewModel.save() },
                    { expenseWithItemsViewModel.validate() }
                )
            }
        }
    }
}