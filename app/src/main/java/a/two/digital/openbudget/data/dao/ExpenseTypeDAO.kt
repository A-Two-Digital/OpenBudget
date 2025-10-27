package a.two.digital.openbudget.data.dao

import a.two.digital.openbudget.data.entity.ExpenseType
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseTypeDAO {
    @Query("SELECT * FROM expenseTypes ORDER BY name ASC")
    fun getAll(): Flow<List<ExpenseType>>

    @Insert
    fun insert(expenseType: ExpenseType)

    @Delete
    fun delete(expenseType: ExpenseType)
}