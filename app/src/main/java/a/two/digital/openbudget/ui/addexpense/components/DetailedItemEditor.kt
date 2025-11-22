package a.two.digital.openbudget.ui.addexpense.components

import a.two.digital.openbudget.data.entity.ExpenseItem
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import a.two.digital.openbudget.logic.ExpenseWithItemsViewModel
import a.two.digital.openbudget.logic.ValidationState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailedItemEditor(
    state: MutableList<ExpenseItem>,
    validation: ValidationState,
    expenseWithItemsViewModel: ExpenseWithItemsViewModel,
    expenseTypeViewModel: ExpenseTypeViewModel
) {
    OutlinedCard(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
            .fillMaxWidth()
            .height(350.dp)
    ) {
        ExpenseItemTitle(onClick = { expenseWithItemsViewModel.addExpenseItem() })


        LazyColumn {
            items(
                state,
                { item -> item.id }
            ) { item ->
                val itemErrors =
                    validation.itemErrors[item.id] ?: emptySet()
                OutlinedCard(
                    modifier = Modifier
                        .padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
//                                                ExpenseTypeSelect(database)
//                                                TextField(R.string.price, R.string.price_placeholder)
//                                                TextField(R.string.description, R.string.description_placeholder)
                }
            }
        }
    }
}