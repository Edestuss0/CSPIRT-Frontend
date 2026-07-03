package com.cpirt.app.features.authorization.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.features.authorization.viewmodel.AuthorizationViewmodel
import com.cpirt.app.ui.theme.AppCard
import com.cpirt.app.ui.theme.AppScaffold
import com.cpirt.app.ui.components.snackbar.AppSnackbarHost
import com.cpirt.app.ui.components.screens.LoadingScreen
import com.cpirt.app.ui.theme.AppTextField
import com.cpirt.app.ui.theme.PrimaryButton

@Composable
fun AuthorizationScreen(
    viewmodel: AuthorizationViewmodel = hiltViewModel()
) {

    val state by viewmodel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.snackbarMessage) {
        state.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewmodel.onMessageShown()
        }
    }

    if (state.isLoading) {
        LoadingScreen()
        return
    }

    AppScaffold(
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AppCard(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Вход в систему",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    label = "Введите имя пользователя",
                    value = state.loginInput,
                    onValueChange = {newValue -> viewmodel.onLoginInput(newValue)},
                )

                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    label = "Введите пароль",
                    value = state.passwordInput,
                    onValueChange = {newValue -> viewmodel.onPasswordInput(newValue)},
                    isError = state.isError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                PrimaryButton(
                    text = "Войти",
                    onClick = {viewmodel.login()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}
