package a.two.digital.openbudget.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NumberField(
    labelText: Int,
    placeholderText: Int,
    isError: Boolean,
    value: Double,
    onValueChange: (CharSequence) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val textFieldState = remember(value) {
        TextFieldState(if (value.toString() == "0.0") "" else value.toString())
    }

    //TODO fix
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }.collectLatest {
            if (it.matches("^\\d+([.,]\\d{1,2})?$".toRegex())) {
                onValueChange(it)
            } else {
                textFieldState.edit {
                    replace(0, it.length, if (value.toString() == "0.0") "" else value.toString())
                }
            }
        }
    }

    OutlinedTextField(
        state = textFieldState,
        label = { Text(text = stringResource(labelText)) },
        labelPosition = TextFieldLabelPosition.Above(),
        placeholder = { Text(text = stringResource(placeholderText)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = { focusManager.clearFocus() }
    )
}