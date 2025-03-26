package com.example.mobg6g60505.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mobg6g60505.R

@Composable
fun LogInScreen(
    userEmail: String,
    isEmailWrong: Boolean,
    checkEmail: () -> Unit,
    onEmailChanged: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userEmail,
                onValueChange = onEmailChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { checkEmail() }
                ),
                singleLine = true,
                label = {
                    if (isEmailWrong) {
                        Text(
                            text = stringResource(R.string.log_in_text_field_error_label),
                            color = colors.error
                        )
                    } else {
                        Text(stringResource(R.string.log_in_text_field_label))
                    }
                },
                isError = isEmailWrong,
                modifier = Modifier
                    .weight(1f)
                    .background(colors.surface, shape = RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.onSurface.copy(alpha = 0.5f),
                    errorBorderColor = colors.error
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = checkEmail,
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text("OK")
            }
        }
    }
}

/**
@Preview
@Composable
fun LogInPreview() {
    LogInScreen(false,modifier = Modifier.fillMaxHeight())
}
 **/