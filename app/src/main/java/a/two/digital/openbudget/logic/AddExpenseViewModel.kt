package a.two.digital.openbudget.logic

import a.two.digital.openbudget.data.dao.ExpenseDAO
import a.two.digital.openbudget.data.dao.ExpenseItemDAO
import a.two.digital.openbudget.data.entity.Expense
import a.two.digital.openbudget.data.entity.ExpenseItem
import a.two.digital.openbudget.logic.model.ExpenseWithItems
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ValidationState(
    val startChecking: Boolean = false,
    val isTitleError: Boolean = false,
    val itemErrors: Map<Long, Set<ItemErrorType>> = emptyMap()
)

enum class ItemErrorType {
    PRICE,
    EXPENSE_TYPE
}

class AddExpenseViewModel(
    private val expenseDAO: ExpenseDAO,
    private val expenseItemDAO: ExpenseItemDAO
) : ViewModel() {
    private val _expenseWithItems = MutableStateFlow(
        ExpenseWithItems(
            expense = Expense(
                title = "",
                date = 0,
                isIncoming = false,
                isRecurring = false
            ),
            items = mutableListOf(
                ExpenseItem(
                    id = UUID.randomUUID().hashCode().toLong(),
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
                    id = UUID.randomUUID().hashCode().toLong(),
                    description = "",
                    price = 0.0,
                    expenseTypeId = -1,
                    expenseId = 0
                )
            )
            currentState.copy(items = newItems)
        }
    }

    fun updateTitle(title: CharSequence) {
        val titleString = title.toString()

        _expenseWithItems.update {
            it.copy(expense = it.expense.copy(title = titleString))
        }

        if (_validationState.value.startChecking) {
            _validationState.update {
                it.copy(isTitleError = title.isBlank())
            }
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

    fun updateIsRecurring(isRecurring: Boolean) {
        _expenseWithItems.update {
            it.copy(expense = it.expense.copy(isRecurring = isRecurring))
        }
    }

    fun updateExpenseItemDescription(id: Long, description: CharSequence) {
        val descriptionString = description.toString()

        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            val index = newItems.indexOfFirst { item -> item.id == id }
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(description = descriptionString)
            }

            it.copy(items = newItems)
        }
    }

    fun updateExpenseItemPrice(id: Long, price: CharSequence) {
        val newPrice = price.toString().toDoubleOrNull() ?: 0.0
        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            val index = newItems.indexOfFirst { item -> item.id == id }
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(price = newPrice)
            }

            it.copy(items = newItems)
        }

        if (_validationState.value.startChecking) {
            if (newPrice <= 0.0) {
                _validationState.update { currentState ->
                    val newMap = currentState.itemErrors.toMutableMap()
                    val currentErrors = newMap[id]?.toMutableSet() ?: mutableSetOf()
                    currentErrors.add(ItemErrorType.PRICE)
                    newMap[id] = currentErrors
                    currentState.copy(itemErrors = newMap)
                }
            } else {
                clearItemError(id, ItemErrorType.PRICE)
            }
        }
    }

    fun updateExpenseItemExpenseType(id: Long, expenseType: Int) {
        _expenseWithItems.update {
            val newItems = it.items.toMutableList()
            val index = newItems.indexOfFirst { item -> item.id == id }
            if (index in newItems.indices) {
                val itemToUpdate = newItems[index]
                newItems[index] = itemToUpdate.copy(expenseTypeId = expenseType)
            }

            it.copy(items = newItems)
        }

        if (_validationState.value.startChecking) {
            if (expenseType == -1) {
                _validationState.update { currentState ->
                    val newMap = currentState.itemErrors.toMutableMap()
                    val currentErrors = newMap[id]?.toMutableSet() ?: mutableSetOf()
                    currentErrors.add(ItemErrorType.EXPENSE_TYPE)
                    newMap[id] = currentErrors
                    currentState.copy(itemErrors = newMap)
                }
            } else {
                clearItemError(id, ItemErrorType.EXPENSE_TYPE)
            }
        }
    }

    fun clearItemError(id: Long, errorType: ItemErrorType) {
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
        viewModelScope.launch {
            val expenseId = expenseDAO.insert(_expenseWithItems.value.expense)
            val itemsToSave = _expenseWithItems.value.items.map {
                it.copy(id = 0, expenseId = expenseId)
            }
            expenseItemDAO.insert(itemsToSave)
        }
    }

    fun validate(): Boolean {
        val currentItems = _expenseWithItems.value.items
        val currentExpense = _expenseWithItems.value.expense

        val newItemErrors = mutableMapOf<Long, MutableSet<ItemErrorType>>()

        currentItems.forEach { item ->
            val errorsForItem = mutableSetOf<ItemErrorType>()
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
            startChecking = true,
            isTitleError = currentExpense.title.isBlank(),
            itemErrors = newItemErrors
        )
        _validationState.value = newState

        return !newState.isTitleError && newState.itemErrors.isEmpty()
    }
}

class AddExpenseViewModelFactory(
    private val expenseDAO: ExpenseDAO,
    private val expenseItemDAO: ExpenseItemDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(expenseDAO, expenseItemDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}