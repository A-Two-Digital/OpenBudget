package a.two.digital.openbudget.ui

import a.two.digital.openbudget.OpenBudgetApplication
import a.two.digital.openbudget.R
import a.two.digital.openbudget.data.AppDatabase
import a.two.digital.openbudget.logic.ExpenseTypeViewModel
import a.two.digital.openbudget.logic.ExpenseTypeViewModelFactory
import a.two.digital.openbudget.logic.ExpenseWithItemsViewModel
import a.two.digital.openbudget.logic.ExpenseWithItemsViewModelFactory
import a.two.digital.openbudget.ui.theme.OpenBudgetTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AddExpenseActivity : ComponentActivity() {

    companion object {
        const val EXTRA_SELECTED_DATE = "extra_selected_day"
    }

    private val database: AppDatabase by lazy {
        (application as OpenBudgetApplication).database
    }

    private val expenseWithItemsViewModel: ExpenseWithItemsViewModel by viewModels {
        ExpenseWithItemsViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val selectedDate = LocalDate.ofEpochDay(
            intent.getLongExtra(
                EXTRA_SELECTED_DATE,
                LocalDate.now().toEpochDay()
            )
        )
        setContent {
            OpenBudgetTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Title()
                            TextField(R.string.title, R.string.title_placeholder)
                            DateTextField(R.string.date, selectedDate)

                            var isDetailed by remember { mutableStateOf(false) }
                            Spacer(modifier = Modifier.padding(vertical = 5.dp))
                            ChoiceSwitch(
                                R.string.simple_or_detailed_label,
                                R.string.simple_or_detailed_description,
                                checked = isDetailed,
                                onCheckedChange = { isDetailed = it }
                            )
                            ChoiceSwitch(
                                R.string.spent_or_gained_label,
                                R.string.spent_or_gained_description,
                                false,
                                {}
                            )
                            ChoiceSwitch(
                                R.string.recurring_label,
                                R.string.recurring_description,
                                false,
                                {})
                            Spacer(modifier = Modifier.padding(vertical = 5.dp))

                            if (!isDetailed) {
                                ExpenseTypeSelect(database)
                                TextField(R.string.price, R.string.price_placeholder)
                                TextField(R.string.description, R.string.description_placeholder)
                            } else {
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

                                    val expenseWithItemsState by expenseWithItemsViewModel.expenseWithItems.collectAsState()

                                    LazyColumn {
                                        items(expenseWithItemsState.items) { item ->
                                            Text(text = "Item: 1")
                                        }
                                    }
                                }
                            }

                            CreateExpenseButton()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Title() {
    val activity = LocalActivity.current as ComponentActivity

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 10.dp)
    ) {
        Icon(
            Icons.Filled.ArrowBackIosNew,
            contentDescription = stringResource(R.string.exit_add_expense_activity),
            Modifier
                .padding(20.dp)
                .size(18.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { activity.finish() }
        )
        Text(
            stringResource(R.string.add_an_expense),
            modifier = Modifier.weight(1f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(18.dp + 40.dp))
    }
}

@Composable
fun ExpenseItemTitle(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
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

@Composable
fun TextField(labelText: Int, placeholderText: Int) {
    OutlinedTextField(
        state = rememberTextFieldState(),
        label = { Text(text = stringResource(labelText)) },
        labelPosition = TextFieldLabelPosition.Above(),
        placeholder = { Text(text = stringResource(placeholderText)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    )
}

@Composable
fun DateTextField(labelText: Int, selectedDate: LocalDate) {
    var showModal by remember { mutableStateOf(false) }
    val textFieldState =
        rememberTextFieldState(selectedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))

    Box {
        OutlinedTextField(
            state = textFieldState,
            label = { Text(text = stringResource(labelText)) },
            labelPosition = TextFieldLabelPosition.Above(),
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            trailingIcon = {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = stringResource(R.string.date_description)
                )
            }
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { showModal = true }
                )
        )
    }

    if (showModal) {
        DatePickerModal(
            onDateSelected = { dateInMillis ->
                dateInMillis?.let {
                    val newDate =
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    textFieldState.setTextAndPlaceCursorAtEnd(
                        newDate.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.LONG
                            )
                        )
                    )
                }
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun ExpenseTypeSelect(database: AppDatabase) {
    val expenseTypeViewModel: ExpenseTypeViewModel = viewModel(
        factory = ExpenseTypeViewModelFactory(database.expenseTypeDao())
    )
    val textFieldState = rememberTextFieldState("")
    var expenseTypesExpanded by remember { mutableStateOf(false) }
    val expenseTypes by expenseTypeViewModel.expenseTypes.collectAsState()
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
                        expenseTypesExpanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceSwitch(
    labelText: Int,
    descriptionText: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(labelText))
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 10.dp)
        )

        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                TooltipAnchorPosition.Above,
                4.dp
            ),
            tooltip = {
                RichTooltip(
                    title = { Text(stringResource(labelText)) }
                ) {
                    Text(stringResource(descriptionText))
                }
            },
            state = tooltipState
        ) {
            IconButton(onClick = {
                coroutineScope.launch {
                    tooltipState.show()
                }
            }) {
                Icon(
                    Icons.AutoMirrored.Outlined.Help,
                    contentDescription = stringResource(descriptionText),
                    modifier = Modifier.alpha(0.5f)
                )
            }
        }

    }
}

@Composable
fun CreateExpenseButton() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        FilledTonalButton(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            onClick = {}
        ) {
            Text(stringResource(R.string.add))
        }
    }
}