package a.two.digital.openbudget.ui.addexpense.components

import a.two.digital.openbudget.R
import a.two.digital.openbudget.data.entity.ExpenseItem
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import a.two.digital.openbudget.logic.ExpenseWithItemsViewModel
import a.two.digital.openbudget.logic.ItemErrorType
import a.two.digital.openbudget.logic.ValidationState
import a.two.digital.openbudget.ui.components.ExpenseTypeSelect
import a.two.digital.openbudget.ui.components.NumberField
import a.two.digital.openbudget.ui.components.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun SimpleItemEditor(
    state: ExpenseItem,
    validation: ValidationState,
    expenseWithItemsViewModel: ExpenseWithItemsViewModel,
    expenseTypeViewModel: ExpenseTypeViewModel
) {
    val itemErrors = validation.itemErrors[state.id] ?: emptySet()
    ExpenseTypeSelect(
        expenseTypeViewModel,
        itemErrors.contains(ItemErrorType.EXPENSE_TYPE),
        state.expenseTypeId
    ) {
        expenseWithItemsViewModel.updateExpenseItemExpenseType(
            state.id,
            it
        )
    }
    NumberField(
        R.string.price,
        R.string.price_placeholder,
        itemErrors.contains(ItemErrorType.PRICE),
        expenseWithItemsViewModel.expenseWithItems.collectAsState().value.items.first().price,
    ) {
        expenseWithItemsViewModel.updateExpenseItemPrice(
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
        expenseWithItemsViewModel.updateExpenseItemDescription(
            state.id,
            it
        )
    }
}