package a.two.digital.openbudget.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TextField(
    labelText: Int,
    placeholderText: Int,
    isError: Boolean,
    value: String,
    onValueChange: (CharSequence) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val textFieldState = rememberTextFieldState(value)

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }.collectLatest {
            onValueChange(it)
        }
    }

    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        label = { Text(text = stringResource(labelText)) },
        labelPosition = TextFieldLabelPosition.Above(),
        placeholder = { Text(text = stringResource(placeholderText)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = { focusManager.clearFocus() }
    )
}