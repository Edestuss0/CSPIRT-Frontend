package com.cpirt.app.features.event.view

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.domain.events.entity.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventHost(
    event: Event?,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val navController = rememberNavController()

    if (event == null) return

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle()},
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.75f)
    ) {
        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                EventDetailsView(event)
            }
        }
    }
}