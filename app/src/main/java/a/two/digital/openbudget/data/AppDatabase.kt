package a.two.digital.openbudget.data

import a.two.digital.openbudget.data.dao.ExpenseDAO
import a.two.digital.openbudget.data.entity.Expense
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expense::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDAO
}