package a.two.digital.openbudget

import a.two.digital.openbudget.data.AppDatabase
import android.app.Application
import androidx.room.Room

class OpenBudgetApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "open-budget"
        ).build()
    }
}