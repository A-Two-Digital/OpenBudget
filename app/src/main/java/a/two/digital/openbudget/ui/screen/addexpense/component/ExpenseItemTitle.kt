package a.two.digital.openbudget.ui.screen.addexpense.component

import a.two.digital.openbudget.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpenseItemTitle(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.size(30.dp + 30.dp))
        Text(
            stringResource(R.string.add_expense_item),
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Surface(
            onClick = { onClick() },
            modifier = Modifier.padding(horizontal = 10.dp),
            shape = CircleShape
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_expense_item),
                Modifier
                    .size(30.dp)
                    .padding(5.dp)
            )
        }
    }
}