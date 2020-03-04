package ru.evilsnow.otus.fcatalog.event

import ru.evilsnow.otus.fcatalog.model.FilmItem
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class FilmItemsEventController : EventController<FilmItem> {

    private val container: Array<MutableSet<FilmItem>> =
        Array(EventType.values().size) { HashSet<FilmItem>(5) }

    override fun publishEvents(events: Array<Event<FilmItem>>) {
        for (event in events ) {
            container[event.eventType.ordinal].add(event.value)
        }
    }

    override fun receiveEventItems(eventType: EventType): List<FilmItem> {
        val eventsHolder = container[eventType.ordinal]
        return if (eventsHolder.size > 0) {
            val result = ArrayList<FilmItem>(eventsHolder)
            eventsHolder.clear()
            result
        } else {
            Collections.emptyList<FilmItem>()
        }
    }

}