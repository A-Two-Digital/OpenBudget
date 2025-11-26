package a.two.digital.openbudget.ui.screen.addexpense.component

import a.two.digital.openbudget.R
import a.two.digital.openbudget.data.entity.ExpenseItem
import a.two.digital.openbudget.logic.AddExpenseViewModel
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import a.two.digital.openbudget.logic.ItemErrorType
import a.two.digital.openbudget.logic.ValidationState
import a.two.digital.openbudget.ui.component.ExpenseTypeSelect
import a.two.digital.openbudget.ui.component.NumberField
import a.two.digital.openbudget.ui.component.TextField
import androidx.compose.runtime.Composable

@Composable
fun SimpleItemEditor(
    state: ExpenseItem,
    validation: ValidationState,
    addExpenseViewModel: AddExpenseViewModel,
    expenseTypeViewModel: ExpenseTypeViewModel
) {
    val itemErrors = validation.itemErrors[state.id] ?: emptySet()
    ExpenseTypeSelect(
        expenseTypeViewModel,
        itemErrors.contains(ItemErrorType.EXPENSE_TYPE),
        state.expenseTypeId
    ) {
        addExpenseViewModel.updateExpenseItemExpenseType(
            state.id,
            it
        )
    }
    NumberField(
        R.string.price,
        R.string.price_placeholder,
        itemErrors.contains(ItemErrorType.PRICE),
        state.price,
    ) {
        addExpenseViewModel.updateExpenseItemPrice(
            state.id,
            it
        )
    }
    TextField(
        R.string.description,
        R.string.description_placeholder,
        false,
        state.description
    ) {
        addExpenseViewModel.updateExpenseItemDescription(
            state.id,
            it
        )
    }
}