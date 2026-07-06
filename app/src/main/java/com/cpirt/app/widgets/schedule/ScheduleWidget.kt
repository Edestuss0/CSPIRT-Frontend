package com.cpirt.app.widgets.schedule

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.schedule.entity.ScheduleDay
import com.cpirt.app.domain.schedule.entity.ScheduleLesson
import com.cpirt.app.domain.schedule.entity.ScheduleLessonType
import com.cpirt.app.domain.schedule.entity.ScheduleView
import com.cpirt.app.domain.schedule.entity.toScheduleView
import com.cpirt.app.ui.theme.DarkBackground
import com.cpirt.app.ui.theme.DarkOnBackground
import com.cpirt.app.ui.theme.DarkOnSurfaceVariant
import com.cpirt.app.ui.theme.DarkSurface
import com.cpirt.app.ui.theme.LightBackground
import com.cpirt.app.ui.theme.LightOnBackground
import com.cpirt.app.ui.theme.LightOnSurfaceVariant
import com.cpirt.app.ui.theme.LightSurface
import com.cpirt.app.widgets.core.WidgetEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

class ScheduleWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        Log.d("WIDGET", "provideGlance called")

        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )

        val meData = entryPoint.getMyClassUseCase()
            .invoke(false)
            .firstOrNull { it is AppResult.Success || it is AppResult.Error }
        val schoolClass = (meData as? AppResult.Success)?.data?.schoolClass

        if (schoolClass == null) {
            provideContent {
                WidgetMessage(
                    title = "Расписание",
                    message = "Войдите в аккаунт, чтобы увидеть уроки"
                )
            }
            return
        }

        val scheduleResult = entryPoint.getSchedule()
            .invoke(schoolClass.id, false)
            .firstOrNull { it is AppResult.Success || it is AppResult.Error }
        val schedule = when (scheduleResult) {
            is AppResult.Success -> scheduleResult.data
            is AppResult.Error -> scheduleResult.data
            else -> null
        }

        provideContent {
            if (schedule == null) {
                WidgetMessage(
                    title = "Расписание",
                    message = "Не удалось загрузить данные"
                )
            } else {
                Widget(
                    scheduleView = schedule.toScheduleView(),
                    dayNumber = currentDayNumber()
                )
            }
        }
    }
}

@Composable
private fun Widget(
    scheduleView: ScheduleView,
    dayNumber: Int
) {
    val schedule = when (dayNumber) {
        1 -> scheduleView.monday
        2 -> scheduleView.tuesday
        3 -> scheduleView.wednesday
        4 -> scheduleView.thursday
        5 -> scheduleView.friday
        6 -> scheduleView.saturday
        7 -> scheduleView.sunday
        else -> scheduleView.monday
    }.sortedBy { it.lessonNumber }
    val visibleLessons = schedule.take(MAX_VISIBLE_LESSONS)
    val hiddenCount = schedule.size - visibleLessons.size

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(20.dp)
            .padding(16.dp)
            .background(widgetBackground),
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Сегодня",
                style = titleStyle()
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
            Text(
                text = lessonCountLabel(schedule.size),
                style = secondaryStyle()
            )
        }

        Spacer(modifier = GlanceModifier.height(10.dp))

        if (schedule.isEmpty()) {
            EmptySchedule()
        } else {
            visibleLessons.forEach { lesson ->
                LessonItem(lesson)
            }
            if (hiddenCount > 0) {
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = "+ ещё $hiddenCount",
                    style = secondaryStyle(),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun LessonItem(lesson: ScheduleLesson) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = lesson.lessonNumber.toString(),
            style = secondaryStyle(),
            maxLines = 1
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Column(modifier = GlanceModifier.fillMaxWidth()) {
            Text(
                text = lesson.subject,
                style = lessonStyle(),
                maxLines = 1
            )
            Text(
                text = lesson.metaLabel(),
                style = secondaryStyle(),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EmptySchedule() {
    Column(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Уроков нет",
            style = lessonStyle(),
            maxLines = 1
        )
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(
            text = "На сегодня расписание пустое",
            style = secondaryStyle(),
            maxLines = 1
        )
    }
}

@Composable
private fun WidgetMessage(
    title: String,
    message: String
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(20.dp)
            .padding(16.dp)
            .background(widgetBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = titleStyle(),
            maxLines = 1
        )
        Spacer(modifier = GlanceModifier.height(6.dp))
        Text(
            text = message,
            style = secondaryStyle(),
            maxLines = 2
        )
    }
}

@Composable
private fun LoadingWidget() {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(20.dp)
            .padding(16.dp)
            .background(widgetBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = GlanceModifier.size(32.dp),
            color = ColorProvider(
                day = LightSurface,
                night = DarkSurface
            )
        )
    }
}

private fun ScheduleLesson.metaLabel(): String {
    val time = listOf(startTime, endTime)
        .filter { it.isNotBlank() }
        .joinToString("-")
        .ifBlank { "Урок $lessonNumber" }
    val roomLabel = if (room > 0) "каб. $room" else null
    return listOfNotNull(time, roomLabel).joinToString(" · ")
}

private fun currentDayNumber(): Int {
    return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> 1
        Calendar.TUESDAY -> 2
        Calendar.WEDNESDAY -> 3
        Calendar.THURSDAY -> 4
        Calendar.FRIDAY -> 5
        Calendar.SATURDAY -> 6
        Calendar.SUNDAY -> 7
        else -> 1
    }
}

private fun lessonCountLabel(count: Int): String {
    val suffix = when {
        count % 100 in 11..14 -> "уроков"
        count % 10 == 1 -> "урок"
        count % 10 in 2..4 -> "урока"
        else -> "уроков"
    }
    return "$count $suffix"
}

private fun titleStyle() = TextStyle(
    color = primaryText,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp
)

private fun lessonStyle() = TextStyle(
    color = primaryText,
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp
)

private fun secondaryStyle() = TextStyle(
    color = secondaryText,
    fontSize = 11.sp
)

private val widgetBackground = ColorProvider(
    day = LightBackground,
    night = DarkBackground
)

private val primaryText = ColorProvider(
    day = LightOnBackground,
    night = DarkOnBackground
)

private val secondaryText = ColorProvider(
    day = LightOnSurfaceVariant,
    night = DarkOnSurfaceVariant
)

private const val MAX_VISIBLE_LESSONS = 3

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 250, heightDp = 110)
@Composable
fun ScheduleWidgetPreview() {
    val view = ScheduleView(
        monday = listOf(
            previewLesson(1, "Математика", "09:00", "09:40", 234),
            previewLesson(2, "Физика", "09:50", "10:30", 218),
            previewLesson(3, "История", "10:45", "11:25", 112),
            previewLesson(4, "Английский язык", "11:35", "12:15", 305)
        ),
        tuesday = emptyList(),
        wednesday = emptyList(),
        thursday = emptyList(),
        friday = emptyList(),
        saturday = emptyList(),
        sunday = emptyList()
    )

    Widget(
        scheduleView = view,
        dayNumber = 1
    )
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 250, heightDp = 110)
@Composable
fun EmptyScheduleWidgetPreview() {
    Widget(
        scheduleView = ScheduleView(
            monday = emptyList(),
            tuesday = emptyList(),
            wednesday = emptyList(),
            thursday = emptyList(),
            friday = emptyList(),
            saturday = emptyList(),
            sunday = emptyList()
        ),
        dayNumber = 1
    )
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 250, heightDp = 110)
@Composable
fun LoadingScheduleWidgetPreview() {
    LoadingWidget()
}

private fun previewLesson(
    number: Int,
    subject: String,
    startTime: String,
    endTime: String,
    room: Int
) = ScheduleLesson(
    id = number,
    type = ScheduleLessonType.CURRENT,
    baseId = number,
    classId = 45,
    className = "9А",
    day = ScheduleDay.MONDAY,
    lessonNumber = number,
    subject = subject,
    teacherId = 45,
    teacher = null,
    room = room,
    startTime = startTime,
    endTime = endTime,
    description = ""
)
