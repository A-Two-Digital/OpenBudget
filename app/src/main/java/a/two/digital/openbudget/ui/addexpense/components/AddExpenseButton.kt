package a.two.digital.openbudget.ui.addexpense.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AddExpenseButton(text: Int, save: () -> Unit, validate: () -> Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        FilledTonalButton(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            onClick = {
                if (validate()) {
                    save()
                } else {

                }
            }
        ) {
            Text(stringResource(text))
        }
    }
}