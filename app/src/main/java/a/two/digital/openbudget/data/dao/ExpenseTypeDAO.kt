package a.two.digital.openbudget.data.dao

import a.two.digital.openbudget.data.entity.ExpenseType
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseTypeDAO {
    @Query("SELECT * FROM expenseTypes")
    fun getAll(): List<ExpenseType>

    @Insert
    fun insert(expenseType: ExpenseType)

    @Delete
    fun delete(expenseType: ExpenseType)
}