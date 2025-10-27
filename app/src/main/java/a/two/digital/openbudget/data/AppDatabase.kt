package a.two.digital.openbudget.data

import a.two.digital.openbudget.data.dao.ExpenseDAO
import a.two.digital.openbudget.data.dao.ExpenseTypeDAO
import a.two.digital.openbudget.data.entity.Expense
import a.two.digital.openbudget.data.entity.ExpenseType
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expense::class, ExpenseType::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDAO
    abstract fun expenseTypeDao(): ExpenseTypeDAO
}