package a.two.digital.openbudget

import a.two.digital.openbudget.data.AppDatabase
import a.two.digital.openbudget.data.entity.ExpenseType
import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OpenBudgetApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "open-budget"
        ).addCallback(populateCallback).fallbackToDestructiveMigration(true).build()
    }

    private val populateCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            CoroutineScope(Dispatchers.IO).launch {
                val expenseTypeDao = database.expenseTypeDao()
                expenseTypeDao.insert(ExpenseType(name = "Housing", color = "#4CAF50"))
                expenseTypeDao.insert(ExpenseType(name = "Utilities", color = "#FF9800"))
                expenseTypeDao.insert(ExpenseType(name = "Groceries", color = "#2196F3"))
                expenseTypeDao.insert(ExpenseType(name = "Transportation", color = "#F44336"))
                expenseTypeDao.insert(ExpenseType(name = "Entertainment", color = "#9C27B0"))
                expenseTypeDao.insert(ExpenseType(name = "Investments", color = "#009688"))
                expenseTypeDao.insert(ExpenseType(name = "Other", color = "#FFC107"))
            }
        }
    }
}