package ru.evilsnow.otus.fcatalog.change

import ru.evilsnow.otus.fcatalog.model.FilmItem
import kotlin.reflect.KClass

interface FilmItemChangeListener {
    fun sendChanges(changes: Array<Change>)
    fun <T : Change> getChanges(changeClass: KClass<T>) : Array<FilmItem>?
    fun hasChanges(changeClass: KClass<Change>) : Boolean
}