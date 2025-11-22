package a.two.digital.openbudget.ui.components

import a.two.digital.openbudget.R
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun ExpenseTypeSelect(
    expenseTypeViewModel: ExpenseTypeViewModel,
    isError: Boolean,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    val expenseTypes by expenseTypeViewModel.expenseTypes.collectAsState()
    val textFieldState =
        rememberTextFieldState(if (value == -1) "" else expenseTypes.find { expenseType -> expenseType.id == value }!!.name)
    var expenseTypesExpanded by remember { mutableStateOf(false) }
    var selectedExpenseTypeSize by remember { mutableStateOf(Size.Zero) }
    val icon =
        if (expenseTypesExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Box {
            OutlinedTextField(
                state = textFieldState,
                label = { Text(stringResource(R.string.expense_type)) },
                labelPosition = TextFieldLabelPosition.Above(),
                placeholder = { Text(stringResource(R.string.expense_type_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        selectedExpenseTypeSize = coordinates.size.toSize()
                    }
                    .focusRequester(focusRequester),
                trailingIcon = {
                    Icon(
                        icon,
                        contentDescription = stringResource(R.string.expense_type_dropdown_arrow)
                    )
                },
                isError = isError,
                readOnly = true
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            expenseTypesExpanded = !expenseTypesExpanded
                            focusRequester.requestFocus()
                        }
                    )
            )
        }
        DropdownMenu(
            expanded = expenseTypesExpanded,
            onDismissRequest = {
                expenseTypesExpanded = false; focusManager.clearFocus()
            },
            modifier = Modifier
                .width(with(LocalDensity.current) { selectedExpenseTypeSize.width.toDp() })
                .height(250.dp)
        ) {
            expenseTypes.forEach { expenseType ->
                DropdownMenuItem(
                    text = { Text(text = expenseType.name) },
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd(expenseType.name)
                        onValueChange(expenseType.id)
                        expenseTypesExpanded = false
                    }
                )
            }
        }
    }
}