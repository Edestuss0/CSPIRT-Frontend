package com.cpirt.app.features.parallels.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.domain.classes.entity.Parallel
import com.cpirt.app.features.parallels.viewmodel.ParallelsViewmodel
import com.cpirt.app.ui.components.LoadingScreen
import com.cpirt.app.ui.components.cards.ParallelCard

@Composable
fun ParallelsScreen(
    viewmodel: ParallelsViewmodel = hiltViewModel()
) {
    val state by viewmodel.state.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
        if (state.isLoading && state.parallels.isNullOrEmpty()) {
            LoadingScreen()
        } else {
            PullToRefreshBox(
                isRefreshing = state.isLoading,
                onRefresh = {viewmodel.loadData(true)},
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                when {
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
                                Spacer(Modifier.height(16.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {viewmodel.loadData(true)},
                                    shape = RoundedCornerShape(16.dp),
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    Text(text = "Повторить попытку", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    else -> {
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
    }
}