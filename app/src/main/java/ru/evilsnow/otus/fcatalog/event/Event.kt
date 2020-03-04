package ru.evilsnow.otus.fcatalog.event

data class Event<T>(val eventType: EventType, val value: T)