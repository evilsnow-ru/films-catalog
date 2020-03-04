package ru.evilsnow.otus.fcatalog.event

interface EventController<T> {
    fun publishEvents(events: Array<Event<T>>)
    fun receiveEventItems(eventType: EventType): List<T>
}