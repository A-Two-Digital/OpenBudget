package a.two.digital.openbudget.logic

import a.two.digital.openbudget.data.entity.Expense
import a.two.digital.openbudget.data.entity.ExpenseItem
import a.two.digital.openbudget.logic.model.ExpenseWithItems
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExpenseWithItemsViewModel : ViewModel() {

    private val _expenseWithItems = MutableStateFlow(
        ExpenseWithItems(
            expense = Expense(
                title = "",
                date = 0,
                isIncoming = false
            ),
            items = mutableListOf(
                ExpenseItem(
                    description = "",
                    price = 0.0,
                    expenseTypeId = 0,
                    expenseId = 0
                )
            )
        )
    )

    var expenseWithItems = _expenseWithItems.asStateFlow()

    fun addExpenseItem() {
        _expenseWithItems.update { currentState ->
            val newItems = currentState.items.toMutableList()
            newItems.add(
                ExpenseItem(
                    description = "",
                    price = 0.0,
                    expenseTypeId = 0,
                    expenseId = 0
                )
            )
            currentState.copy(items = newItems)
        }
    }
}

class ExpenseWithItemsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseWithItemsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseWithItemsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}