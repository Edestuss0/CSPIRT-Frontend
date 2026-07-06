package com.cpirt.app.features.event.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.user.entity.UserRole
import com.cpirt.app.features.event.viewmodel.EventViewModel
import com.cpirt.app.ui.theme.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventHost(
    event: Event?,
    onDismiss: () -> Unit,
    viewModel: EventViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()
    var currentClass by remember { mutableStateOf<SchoolClass?>(null) }

    if (event == null) return

    LaunchedEffect(event) {
        viewModel.loadEvent(event)
        viewModel.loadData(false)
    }

    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle()},
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.75f),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            NavHost(
                navController = navController,
                startDestination = "main"
            ) {
                composable("main") {
                    EventDetailsView(
                        event = event,
                        onClassesClick = {navController.navigate("classes")}
                    )
                }
                composable("classes") {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(16.dp)) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Назад",
                                tint = Color.White
                            )
                        }
                        EventClassesView(
                            classes = state.eventClasses,
                            onClassClick = { schoolClass ->
                                if (state.profileInfo?.classId == schoolClass.id && (state.profileInfo?.role == UserRole.Admin || state.profileInfo?.role == UserRole.Owner)) {
                                    currentClass = schoolClass
                                    navController.navigate("players")
                                    viewModel.onPlayersViewToggle(schoolClass)
                                    viewModel.loadEvent(event)
                                }
                            }
                        )
                    }
                }
                composable("players") {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(16.dp)) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Назад",
                                tint = Color.White
                            )
                        }
                        if (currentClass == null) {
                            EmptyState("Не удалось получить информацию о классе")
                        } else {
                            EventPlayersView(
                                schoolClass = currentClass!!,
                                viewModel = viewModel,
                                playersState = state.eventPlayersState,
                                onSubmit = {navController.popBackStack()}
                            )
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}