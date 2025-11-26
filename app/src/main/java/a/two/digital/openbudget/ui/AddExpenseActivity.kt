package a.two.digital.openbudget.ui

import a.two.digital.openbudget.OpenBudgetApplication
import a.two.digital.openbudget.data.AppDatabase
import a.two.digital.openbudget.logic.AddExpenseViewModel
import a.two.digital.openbudget.logic.AddExpenseViewModelFactory
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import a.two.digital.openbudget.logic.ExpenseTypeViewModelFactory
import a.two.digital.openbudget.ui.screen.addexpense.AddExpenseScreen
import a.two.digital.openbudget.ui.theme.OpenBudgetTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import java.time.LocalDate

class AddExpenseActivity : ComponentActivity() {

    companion object {
        const val EXTRA_SELECTED_DATE = "extra_selected_day"
    }

    private val database: AppDatabase by lazy {
        (application as OpenBudgetApplication).database
    }

    private val addExpenseViewModel: AddExpenseViewModel by viewModels {
        AddExpenseViewModelFactory(database.expenseDao(), database.expenseItemDao())
    }

    private val expenseTypeViewModel: ExpenseTypeViewModel by viewModels {
        ExpenseTypeViewModelFactory(database.expenseTypeDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addExpenseViewModel.updateDate(
            intent.getLongExtra(
                EXTRA_SELECTED_DATE,
                LocalDate.now().toEpochDay()
            )
        )

        setContent {
            OpenBudgetTheme {
                AddExpenseScreen(
                    addExpenseViewModel,
                    expenseTypeViewModel
                ) { finish() }
            }
        }
    }
}