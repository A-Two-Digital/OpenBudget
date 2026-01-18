package a.two.digital.openbudget.ui.screen.addexpense.component

import a.two.digital.openbudget.R
import a.two.digital.openbudget.data.entity.ExpenseItem
import a.two.digital.openbudget.logic.AddExpenseViewModel
import a.two.digital.openbudget.logic.ItemErrorType
import a.two.digital.openbudget.logic.ValidationState
import a.two.digital.openbudget.ui.component.NumberField
import a.two.digital.openbudget.ui.component.TextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailedItemEditor(
    state: MutableList<ExpenseItem>,
    validation: ValidationState,
    addExpenseViewModel: AddExpenseViewModel,
) {
    OutlinedCard(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
            .fillMaxWidth()
            .heightIn(min = 350.dp, max = 500.dp)
    ) {
        ExpenseItemTitle(onClick = { addExpenseViewModel.addExpenseItem() })

        LazyColumn {
            items(
                state,
                { item -> item.id }
            ) { item ->
                val itemErrors = validation.itemErrors[item.id] ?: emptySet()

                NumberField(
                    R.string.price,
                    R.string.price_placeholder,
                    itemErrors.contains(ItemErrorType.PRICE),
                    item.price,
                ) {
                    addExpenseViewModel.updateExpenseItemPrice(
                        item.id,
                        it
                    )
                }
                TextField(
                    R.string.description,
                    R.string.description_placeholder,
                    false,
                    item.description
                ) {
                    addExpenseViewModel.updateExpenseItemDescription(
                        item.id,
                        it
                    )
                }

                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        }
    }
}