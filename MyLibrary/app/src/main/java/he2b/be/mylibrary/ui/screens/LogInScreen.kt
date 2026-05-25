package he2b.be.mylibrary.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import he2b.be.mylibrary.R
import he2b.be.mylibrary.ui.viewmodels.LogInViewModel

@Composable
fun LoginScreen(
    viewModel: LogInViewModel,
    navController: NavController
) {
    val emailState = viewModel.userEmail
    val passwordState = viewModel.userPassword
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val logInSuccess by viewModel.logInSuccess.collectAsState()

    val emptyEmailError = stringResource(R.string.error_empty_email)
    val invalidEmailError = stringResource(R.string.error_invalid_email)
    val missingPasswordMessage = stringResource(R.string.error_empty_password)
    val invalidCredentialsMessage = stringResource(R.string.invalid_credentials_error)
    val unknownError = stringResource(R.string.unknown_error)

    LaunchedEffect(logInSuccess) {
        if (logInSuccess) {
            viewModel.onLoginSuccess(navController)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.book_stack_icon__icon_search_engine_16),
                contentDescription = "Logo",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(100.dp)
                    .clip(RectangleShape)
            )
            Spacer(modifier = Modifier.height(32.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(0.8f),
                    textAlign = TextAlign.Center,
                )
            }

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { viewModel.updateEmail(it) },
                label = { Text(stringResource(R.string.email_field)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 6.dp)
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text(stringResource(R.string.password_field)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.authenticate(
                        emptyEmailError = emptyEmailError,
                        invalidEmailError = invalidEmailError,
                        missingPasswordMessage = missingPasswordMessage,
                        invalidCredentialsMessage = invalidCredentialsMessage,
                        unknownErrorMessage = unknownError
                    )
                },
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
                } else {
                    Text(stringResource(R.string.log_in_button))
                }
            }

            TextButton(
                onClick = { viewModel.onNavigateToSignUp(navController) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(stringResource(R.string.to_sign_up_text), textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}