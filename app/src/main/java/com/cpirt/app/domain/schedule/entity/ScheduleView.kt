package com.cpirt.app.domain.schedule.entity

data class ScheduleView(
    val monday: List<ScheduleLesson>,
    val tuesday: List<ScheduleLesson>,
    val wednesday: List<ScheduleLesson>,
    val thursday: List<ScheduleLesson>,
    val friday: List<ScheduleLesson>,
    val saturday: List<ScheduleLesson>,
    val sunday: List<ScheduleLesson>,
)

fun List<ScheduleLesson>.toScheduleView(): ScheduleView {
    val mondayList = mutableListOf<ScheduleLesson>()
    val tuesdayList = mutableListOf<ScheduleLesson>()
    val wednesdayList = mutableListOf<ScheduleLesson>()
    val thursdayList = mutableListOf<ScheduleLesson>()
    val fridayList = mutableListOf<ScheduleLesson>()
    val saturdayList = mutableListOf<ScheduleLesson>()
    val sundayList = mutableListOf<ScheduleLesson>()

    this.forEach {
        when (it.day) {
            ScheduleDay.MONDAY -> mondayList.add(it)
            ScheduleDay.TUESDAY -> tuesdayList.add(it)
            ScheduleDay.WEDNESDAY -> wednesdayList.add(it)
            ScheduleDay.THURSDAY -> thursdayList.add(it)
            ScheduleDay.FRIDAY -> fridayList.add(it)
            ScheduleDay.SATURDAY -> saturdayList.add(it)
            ScheduleDay.SUNDAY -> sundayList.add(it)
        }
    }
    return ScheduleView(
        monday = mondayList,
        tuesday = tuesdayList,
        wednesday = wednesdayList,
        thursday = thursdayList,
        friday = fridayList,
        saturday = saturdayList,
        sunday = sundayList
    )
}