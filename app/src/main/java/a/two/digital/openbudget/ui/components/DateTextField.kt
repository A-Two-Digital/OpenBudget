package a.two.digital.openbudget.ui.components

import a.two.digital.openbudget.R
import a.two.digital.openbudget.ui.DatePickerModal
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun DateTextField(labelText: Int, value: Long, onDateChange: (Long) -> Unit) {
    LocalFocusManager.current.clearFocus()
    val selectedDate = LocalDate.ofEpochDay(value)
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
                    onDateChange(newDate.toEpochDay())
                }
            },
            onDismiss = { showModal = false }
        )
    }
}