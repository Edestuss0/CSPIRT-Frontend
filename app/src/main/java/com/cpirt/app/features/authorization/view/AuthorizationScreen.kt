package com.cpirt.app.features.authorization.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.features.authorization.viewmodel.AuthorizationViewmodel
import com.cpirt.app.ui.components.AppSnackbarHost
import com.cpirt.app.ui.components.LoadingScreen

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

    Scaffold(
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Вход в систему",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    label = { Text("Введите имя пользователя") },
                    shape = RoundedCornerShape(16.dp),
                    value = state.loginInput,
                    onValueChange = {newValue -> viewmodel.onLoginInput(newValue)},
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    label = { Text("Введите пароль") },
                    shape = RoundedCornerShape(16.dp),
                    value = state.passwordInput,
                    onValueChange = {newValue -> viewmodel.onPasswordInput(newValue)},
                    isError = state.isError
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {viewmodel.login()},
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(text = "Войти", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}