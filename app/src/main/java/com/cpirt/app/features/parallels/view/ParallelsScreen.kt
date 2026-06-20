package com.cpirt.app.features.parallels.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.entities.Parallel
import com.cpirt.app.features.parallels.viewmodel.ParallelsViewmodel
import com.cpirt.app.ui.components.LoadingScreen
import com.cpirt.app.ui.components.cards.ParallelCard

@Composable
fun ParallelsScreen(
    viewmodel: ParallelsViewmodel = hiltViewModel()
) {
    val state by viewmodel.state.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
        when {
            state.isLoading -> {
                LoadingScreen()
            }
            state.parallels.isNullOrEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Параллели не найдены",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            state.parallels != null && state.parallels!!.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)
                ) {
                   items(items = state.parallels as List<Parallel>) {
                       ParallelCard(
                           parallel = it,
                           onParallelClick = {},
                           modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                       )
                   }
                }
            }
        }
    }
}