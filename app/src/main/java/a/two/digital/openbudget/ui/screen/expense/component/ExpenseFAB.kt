package a.two.digital.openbudget.ui.screen.expense.component

import a.two.digital.openbudget.R
import a.two.digital.openbudget.ui.AddExpenseActivity
import a.two.digital.openbudget.ui.theme.Purple40
import a.two.digital.openbudget.ui.theme.Purple80
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import java.time.LocalDate

@Composable
fun ExpenseFAB(selectedDate: LocalDate) {
    val context = LocalContext.current

    FloatingActionButton(
        onClick = {
            val intent = Intent(context, AddExpenseActivity::class.java)
            intent.putExtra(AddExpenseActivity.EXTRA_SELECTED_DATE, selectedDate.toEpochDay())
            context.startActivity(intent)
        },
        containerColor = Purple40,
        contentColor = Purple80
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_an_expense)
        )
    }
}