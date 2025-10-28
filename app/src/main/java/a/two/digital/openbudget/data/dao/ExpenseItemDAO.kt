package a.two.digital.openbudget.data.dao

import a.two.digital.openbudget.data.entity.ExpenseItem
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface ExpenseItemDAO {
    @Insert
    fun insert(expenseItem: ExpenseItem)

    @Delete
    fun delete(expenseItem: ExpenseItem)
}