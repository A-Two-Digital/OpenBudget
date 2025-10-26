package a.two.digital.openbudget.data.dao

import a.two.digital.openbudget.data.entity.Expense
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDAO {
    @Query("SELECT * FROM expenses")
    fun getAll(): List<Expense>

    @Insert
    fun insert(expense: Expense)

    @Delete
    fun delete(expense: Expense)
}