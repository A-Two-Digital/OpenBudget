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
import java.util.UUID

data class ValidationState(
    val isTitleError: Boolean = false,
    val itemErrors: Map<Int, Set<ItemErrorType>> = emptyMap()
)

enum class ItemErrorType {
    DESCRIPTION,
    PRICE,
    EXPENSE_TYPE
}

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
                    id = UUID.randomUUID().hashCode(),
                    description = "",
                    price = 0.0,
                    expenseTypeId = -1,
                    expenseId = 0
                )
            )
        )
    )
    var expenseWithItems = _expenseWithItems.asStateFlow()

    private val _validationState = MutableStateFlow(ValidationState())
    var validationState = _validationState.asStateFlow()

    fun addExpenseItem() {
        _expenseWithItems.update { currentState ->
            val newItems = currentState.items.toMutableList()
            newItems.add(
                ExpenseItem(
                    id = UUID.randomUUID().hashCode(),
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

        _validationState.update {
            it.copy(isTitleError = title.isBlank())
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

    fun updateExpenseItemDescription(id: Int, description: String) {
        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            val index = newItems.indexOfFirst { item -> item.id == id }
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(description = description)
            }

            it.copy(items = newItems)
        }

        if (description.isNotBlank()) {
            clearItemError(id, ItemErrorType.DESCRIPTION)
        }
    }

    fun updateExpenseItemPrice(id: Int, price: Double) {
        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            val index = newItems.indexOfFirst { item -> item.id == id }
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(price = price)
            }

            it.copy(items = newItems)
        }

        if (price > 0.0) {
            clearItemError(id, ItemErrorType.PRICE)
        }
    }

    fun updateExpenseItemExpenseType(id: Int, expenseType: Int) {
        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            val index = newItems.indexOfFirst { item -> item.id == id }
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(expenseTypeId = expenseType)
            }

            it.copy(items = newItems)
        }

        if (expenseType != -1) {
            clearItemError(id, ItemErrorType.EXPENSE_TYPE)
        }
    }

    fun clearItemError(id: Int, errorType: ItemErrorType) {
        _validationState.update { currentState ->
            val currentItemErrors = currentState.itemErrors[id] ?: return@update currentState

            val newItemErrors = currentItemErrors.toMutableSet()
            newItemErrors.remove(errorType)

            val newMap = currentState.itemErrors.toMutableMap()
            if (newItemErrors.isEmpty()) {
                newMap.remove(id)
            } else {
                newMap[id] = newItemErrors
            }

            currentState.copy(itemErrors = newMap)
        }
    }

    fun save() {
        Log.d("ExpenseWithItemsViewModel", "Saving expense with items: ${expenseWithItems.value}")
    }

    fun validate(): Boolean {
        val currentItems = _expenseWithItems.value.items
        val currentExpense = _expenseWithItems.value.expense

        val newItemErrors = mutableMapOf<Int, MutableSet<ItemErrorType>>()

        currentItems.forEach { item ->
            val errorsForItem = mutableSetOf<ItemErrorType>()
            if (item.description.isBlank()) {
                errorsForItem.add(ItemErrorType.DESCRIPTION)
            }
            if (item.price <= 0.0) {
                errorsForItem.add(ItemErrorType.PRICE)
            }
            if (item.expenseTypeId == -1) {
                errorsForItem.add(ItemErrorType.EXPENSE_TYPE)
            }

            if (errorsForItem.isNotEmpty()) {
                newItemErrors[item.id] = errorsForItem
            }
        }

        val newState = ValidationState(
            isTitleError = currentExpense.title.isBlank(),
            itemErrors = newItemErrors
        )
        _validationState.value = newState

        return !newState.isTitleError && newState.itemErrors.isEmpty()
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