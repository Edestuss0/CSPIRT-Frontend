package com.cpirt.app.features.classes.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.features.class_details.view.ClassDetailHost
import com.cpirt.app.features.classes.viewmodel.ClassesUIEventState
import com.cpirt.app.features.classes.viewmodel.ClassesViewModel
import com.cpirt.app.ui.components.cards.ClassCard
import com.cpirt.app.ui.components.screens.LoadingScreen
import com.cpirt.app.ui.components.snackbar.AppSnackbarVisuals
import com.cpirt.app.ui.components.snackbar.SnackbarMessageType
import com.cpirt.app.ui.theme.AppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesScreen(
    viewmodel: ClassesViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onUsersClick: (id: Int) -> Unit
) {
    val state by viewmodel.state.collectAsState()
    var selectedClassId by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewmodel.events.collect { event ->
            when {
                event is ClassesUIEventState.ShowError -> {
                    snackbarHostState.showSnackbar(AppSnackbarVisuals(
                        type = SnackbarMessageType.ERROR,
                        message = event.message
                    ))
                }
                event is ClassesUIEventState.ShowSuccess -> {
                    snackbarHostState.showSnackbar(AppSnackbarVisuals(
                        type = SnackbarMessageType.SUCCESS,
                        message = event.message
                    ))
                }
            }
        }
    }

    AppScaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize().blur(if (selectedClassId != null) 20.dp else 0.dp),
    ) { innerPadding ->

        when {
            state.classesData.isNullOrEmpty() && state.isLoading -> {
                LoadingScreen()
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = state.classesData!!) {
                        ClassCard(
                            schoolClass = it,
                            onClick = {selectedClassId = it.id},
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        if (selectedClassId != null) {
            ClassDetailHost(
                onDismiss = { selectedClassId = null },
                id = selectedClassId,
                onUserClick = onUsersClick
            )
        }
    }
}
