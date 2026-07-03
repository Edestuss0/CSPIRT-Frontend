package com.cpirt.app.features.class_details.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.schedule.entity.ScheduleDay
import com.cpirt.app.domain.schedule.entity.ScheduleView
import com.cpirt.app.domain.schedule.entity.toLabel
import com.cpirt.app.ui.components.cards.ScheduleCard
import com.cpirt.app.ui.theme.EmptyState

@Composable
fun ClassScheduleView(
    scheduleData: ScheduleView?
) {
    if (scheduleData == null) {
        EmptyState("Не удалось получить информацию о расписании")
        return
    }

    val pagerState = rememberPagerState(pageCount = { 7 })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val day = when (page) {
            0 -> ScheduleDay.MONDAY.toLabel()
            1 -> ScheduleDay.TUESDAY.toLabel()
            2 -> ScheduleDay.WEDNESDAY.toLabel()
            3 -> ScheduleDay.THURSDAY.toLabel()
            4 -> ScheduleDay.FRIDAY.toLabel()
            5 -> ScheduleDay.SATURDAY.toLabel()
            6 -> ScheduleDay.SUNDAY.toLabel()
            else -> "Неизвестный день"
        }

        val schedule = when (page) {
            0 -> scheduleData.monday
            1 -> scheduleData.tuesday
            2 -> scheduleData.wednesday
            3 -> scheduleData.thursday
            4 -> scheduleData.friday
            5 -> scheduleData.saturday
            6 -> scheduleData.sunday
            else -> emptyList()
        }

        if (schedule.isNullOrEmpty()) {
            EmptyState("$day - уроки не найдены")
            return@HorizontalPager
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = schedule) { lesson ->
                    ScheduleCard(lesson)
                }
            }
        }

    }

}