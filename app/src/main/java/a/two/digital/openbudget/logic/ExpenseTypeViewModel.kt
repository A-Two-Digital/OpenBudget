package a.two.digital.openbudget.logic

import a.two.digital.openbudget.data.dao.ExpenseTypeDAO
import a.two.digital.openbudget.data.entity.ExpenseType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ExpenseTypeViewModel(expenseTypeDao: ExpenseTypeDAO) : ViewModel() {

    val expenseTypes: StateFlow<List<ExpenseType>> = expenseTypeDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

class ExpenseTypeViewModelFactory(private val expenseTypeDAO: ExpenseTypeDAO) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseTypeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseTypeViewModel(expenseTypeDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}