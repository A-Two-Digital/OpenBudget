package a.two.digital.openbudget.data.dao

import a.two.digital.openbudget.data.entity.Expense
import a.two.digital.openbudget.logic.model.ExpenseWithItems
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDAO {
    @Transaction
    @Query("SELECT * FROM expenses")
    fun getAll(): List<ExpenseWithItems>

    @Transaction
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startOfDay AND :endOfDay")
    fun getAllByDate(startOfDay: Long, endOfDay: Long): Flow<List<ExpenseWithItems>>

    @Insert
    fun insert(expense: Expense)

    @Delete
    fun delete(expense: Expense)
}