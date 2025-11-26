package a.two.digital.openbudget.logic

import a.two.digital.openbudget.data.dao.ExpenseDAO
import a.two.digital.openbudget.logic.model.ExpenseWithItems
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow

class ExpenseListViewModel(
    private val expenseDao: ExpenseDAO
) : ViewModel() {

    fun getExpensesForDate(date: Long): Flow<List<ExpenseWithItems>> {
        return expenseDao.getAllForDate(date)
    }
}

class ExpenseListViewModelFactory(
    private val expenseDAO: ExpenseDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseListViewModel(expenseDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}