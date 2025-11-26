package a.two.digital.openbudget.ui

import a.two.digital.openbudget.OpenBudgetApplication
import a.two.digital.openbudget.data.AppDatabase
import a.two.digital.openbudget.logic.ExpenseListViewModel
import a.two.digital.openbudget.logic.ExpenseListViewModelFactory
import a.two.digital.openbudget.ui.screen.expenselist.ExpenseScreen
import a.two.digital.openbudget.ui.theme.OpenBudgetTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

class ExpenseListActivity : ComponentActivity() {

    private val database: AppDatabase by lazy {
        (application as OpenBudgetApplication).database
    }

    private val expenseListViewModel: ExpenseListViewModel by viewModels {
        ExpenseListViewModelFactory(database.expenseDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenBudgetTheme {
                ExpenseScreen(expenseListViewModel)
            }
        }
    }
}