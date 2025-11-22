package a.two.digital.openbudget.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

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

    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        label = { Text(text = stringResource(labelText)) },
        labelPosition = TextFieldLabelPosition.Above(),
        placeholder = { Text(text = stringResource(placeholderText)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        inputTransformation = {
            onValueChange(textFieldState.text)
        },
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = { focusManager.clearFocus() }
    )
}