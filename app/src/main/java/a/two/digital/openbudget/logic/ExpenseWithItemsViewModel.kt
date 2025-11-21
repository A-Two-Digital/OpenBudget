package a.two.digital.openbudget.logic

import a.two.digital.openbudget.data.entity.Expense
import a.two.digital.openbudget.data.entity.ExpenseItem
import a.two.digital.openbudget.logic.model.ExpenseWithItems
import android.util.Log
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
                    expenseTypeId = -1,
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
                    expenseTypeId = -1,
                    expenseId = 0
                )
            )
            currentState.copy(items = newItems)
        }
    }

    fun updateTitle(title: String) {
        _expenseWithItems.update {
            it.copy(expense = it.expense.copy(title = title))
        }
    }

    fun updateDate(date: Long) {
        _expenseWithItems.update {
            it.copy(expense = it.expense.copy(date = date))
        }
    }

    fun updateIsIncoming(isIncoming: Boolean) {
        _expenseWithItems.update {
            it.copy(expense = it.expense.copy(isIncoming = isIncoming))
        }
    }

    fun updateExpenseItemDescription(index: Int, description: String) {
        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(description = description)
            }

            it.copy(items = newItems)
        }
    }

    fun updateExpenseItemPrice(index: Int, price: Double) {
        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(price = price)
            }

            it.copy(items = newItems)
        }
    }

    fun updateExpenseItemExpenseType(index: Int, expenseType: Int) {
        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(expenseTypeId = expenseType)
            }

            it.copy(items = newItems)
        }
    }

    fun save() {
        Log.d("ExpenseWithItemsViewModel", "Saving expense with items: ${expenseWithItems.value}")
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