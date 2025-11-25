package a.two.digital.openbudget.ui

import a.two.digital.openbudget.ui.screen.expense.ExpenseScreen
import a.two.digital.openbudget.ui.theme.OpenBudgetTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenBudgetTheme {
                ExpenseScreen()
            }
        }
    }
}