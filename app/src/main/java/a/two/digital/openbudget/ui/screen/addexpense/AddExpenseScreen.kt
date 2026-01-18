package a.two.digital.openbudget.ui.screen.addexpense

import a.two.digital.openbudget.R
import a.two.digital.openbudget.logic.AddExpenseViewModel
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import a.two.digital.openbudget.ui.component.ChoiceSwitch
import a.two.digital.openbudget.ui.component.DateTextField
import a.two.digital.openbudget.ui.component.ExpenseTypeSelect
import a.two.digital.openbudget.ui.component.TextField
import a.two.digital.openbudget.ui.screen.addexpense.component.AddExpenseButton
import a.two.digital.openbudget.ui.screen.addexpense.component.AddExpenseTitle
import a.two.digital.openbudget.ui.screen.addexpense.component.DetailedItemEditor
import a.two.digital.openbudget.ui.screen.addexpense.component.SimpleItemEditor
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
    addExpenseViewModel: AddExpenseViewModel,
    expenseTypeViewModel: ExpenseTypeViewModel,
    onClose: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val state by addExpenseViewModel.expenseWithItems.collectAsState()
    val validation by addExpenseViewModel.validationState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() }
            ),
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    R.string.title,
                    R.string.title_placeholder,
                    validation.startChecking && validation.isTitleError,
                    state.expense.title
                ) { addExpenseViewModel.updateTitle(it) }
                DateTextField(
                    R.string.date,
                    state.expense.date
                ) { addExpenseViewModel.updateDate(it) }

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
                ) { addExpenseViewModel.updateIsIncoming(it) }
                ChoiceSwitch(
                    R.string.recurring_label,
                    R.string.recurring_description,
                    state.expense.isRecurring
                ) { addExpenseViewModel.updateIsRecurring(it) }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                ExpenseTypeSelect(
                    expenseTypeViewModel,
                    validation.startChecking && validation.isExpenseTypeError,
                    state.expense.expenseTypeId
                ) {
                    addExpenseViewModel.updateExpenseType(it)
                }

                if (!isDetailed) {
                    SimpleItemEditor(
                        state.items.first(),
                        validation,
                        addExpenseViewModel
                    )
                } else {
                    DetailedItemEditor(
                        state.items,
                        validation,
                        addExpenseViewModel
                    )
                }

                AddExpenseButton(
                    R.string.add,
                    { addExpenseViewModel.save() },
                    { addExpenseViewModel.validate() },
                    { onClose() }
                )
            }
        }
    }
}