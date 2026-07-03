package com.cpirt.app.features.class_details.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.features.class_details.viewmodel.ClassDetailViewModel
import com.cpirt.app.ui.components.screens.ClassUsersScreen
import com.cpirt.app.ui.components.screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailHost(
    onDismiss: () -> Unit,
    id: Int?,
    viewModel: ClassDetailViewModel = hiltViewModel(),
    onUserClick: (id: Int) -> Unit
) {
    val navController = rememberNavController()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        if (id != null) {
            viewModel.loadData(id)
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }
    if (id == null) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.75f)
    ) {
        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                if (state.isLoading && state.classData == null) {
                    LoadingScreen()
                    return@composable
                }

                ClassDetailsSheet(state, {navController.navigate("users")}, {navController.navigate("schedule")})
            }

            composable("users") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row() {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Назад",
                                tint = Color.White
                            )
                        }
                    }
                    ClassUsersScreen(
                        users = state.classData?.members ?: emptyList(),
                        onUserClick = { id -> onUserClick(id) },
                        innerPadding = PaddingValues()
                    )
                }
            }

            composable("schedule") {
                viewModel.loadSchedule(id, false)

                if (state.isLoading && state.scheduleData == null) {
                    LoadingScreen()
                    return@composable
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row() {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Назад",
                                tint = Color.White
                            )
                        }
                    }
                    ClassScheduleView(state.scheduleData)
                }
            }
        }
    }
}