package com.cpirt.app.domain.events.entity

data class EventsView(
    val scheduled: List<Event>,
    val active: List<Event>,
    val completed: List<Event>
)

fun List<Event>.toEventsView(): EventsView {
    val toScheduled = mutableListOf<Event>()
    val toActive = mutableListOf<Event>()
    val toCompleted = mutableListOf<Event>()
    this.forEach {
        when (it.status) {
            EventStatus.SCHEDULED -> toScheduled.add(it)
            EventStatus.UNKNOWN -> {}
            EventStatus.COMPLETED -> toCompleted.add(it)
            EventStatus.ACTIVE -> toActive.add(it)
        }
    }
    return EventsView(
        scheduled = toScheduled,
        active = toActive,
        completed = toCompleted
    )
}
