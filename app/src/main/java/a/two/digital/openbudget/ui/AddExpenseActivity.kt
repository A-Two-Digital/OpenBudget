package a.two.digital.openbudget.ui

import a.two.digital.openbudget.R
import a.two.digital.openbudget.data.AppDatabase
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import a.two.digital.openbudget.logic.ExpenseTypeViewModelFactory
import a.two.digital.openbudget.ui.ui.theme.OpenBudgetTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

class AddExpenseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenBudgetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AddExpenseDialog(onDismissRequest: () -> Unit, database: AppDatabase) {
    val expenseTypeViewModel: ExpenseTypeViewModel = viewModel(
        factory = ExpenseTypeViewModelFactory(database.expenseTypeDao())
    )

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //region Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.add_an_expense),
                        Modifier
                            .weight(1f)
                            .padding(start = 40.dp),
                        fontSize = 16.5.sp,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close_add_expense_dialog),
                        Modifier
                            .padding(12.dp)
                            .size(20.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) { onDismissRequest() }
                    )
                }
                HorizontalDivider(thickness = 1.5.dp)
                //endregion

                OutlinedTextField(
                    state = rememberTextFieldState(),
                    label = { Text(stringResource(R.string.ok)) })
                OutlinedTextField(state = rememberTextFieldState(), label = { Text("Label") })
                OutlinedTextField(state = rememberTextFieldState(), label = { Text("Label") })
                OutlinedTextField(state = rememberTextFieldState(), label = { Text("Label") })

                //region Expense Type
                var expenseTypesExpanded by remember { mutableStateOf(false) }
                val expenseTypes by expenseTypeViewModel.expenseTypes.collectAsState()
                var selectedExpenseType by remember { mutableStateOf("") }
                var selectedExpenseTypeSize by remember { mutableStateOf(Size.Zero) }
                val icon =
                    if (expenseTypesExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Box {
                        OutlinedTextField(
                            value = selectedExpenseType,
                            onValueChange = { selectedExpenseType = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    selectedExpenseTypeSize = coordinates.size.toSize()
                                }
                                .focusRequester(focusRequester),
                            label = { Text(stringResource(R.string.expense_type)) },
                            trailingIcon = {
                                Icon(
                                    icon,
                                    contentDescription = stringResource(R.string.expense_type_dropdown_arrow)
                                )
                            },
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
                            .height(200.dp)
                    ) {
                        expenseTypes.forEach { expenseType ->
                            DropdownMenuItem(
                                text = { Text(text = expenseType.name) },
                                onClick = {
                                    selectedExpenseType = expenseType.name; expenseTypesExpanded =
                                    false
                                }
                            )
                        }
                    }
                }
                //endregion
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenBudgetTheme {
        Greeting("Android")
    }
}