package com.cpirt.app.features.parallels.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.domain.classes.entity.Parallel
import com.cpirt.app.features.parallels.viewmodel.ParallelsViewmodel
import com.cpirt.app.ui.theme.AppScaffold
import com.cpirt.app.ui.theme.EmptyState
import com.cpirt.app.ui.theme.PrimaryButton
import com.cpirt.app.ui.components.cards.ParallelCard
import com.cpirt.app.ui.components.screens.LoadingScreen

@Composable
fun ParallelsScreen(
    viewmodel: ParallelsViewmodel = hiltViewModel(),
    onParallelClick: (id: Int) -> Unit
) {
    val state by viewmodel.state.collectAsState()

    AppScaffold(
        modifier = Modifier.fillMaxSize()
    ) {innerPadding ->
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
                                EmptyState(text = "Параллели не найдены")
                                Spacer(Modifier.height(16.dp))
                                PrimaryButton(
                                    text = "Повторить попытку",
                                    onClick = {viewmodel.loadData(true)},
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
                            ) {
                                items(items = state.parallels as List<Parallel>) {
                                    ParallelCard(
                                        parallel = it,
                                        onParallelClick = {onParallelClick(it.id)},
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
