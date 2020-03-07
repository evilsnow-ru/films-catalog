package ru.evilsnow.otus.fcatalog.change

import ru.evilsnow.otus.fcatalog.model.FilmItem
import kotlin.reflect.KClass

class SimpleFilmItemChangeListener : FilmItemChangeListener {

    private val mAddHolder: MutableSet<FilmItem> = LinkedHashSet()
    private val mRemoveHolder: MutableSet<FilmItem> = LinkedHashSet()

    override fun sendChanges(changes: Array<Change>) {
        for (change in changes) {
            when(change) {
                is Change.AddFavorite -> mAddHolder.add(change.filmItem)
                is Change.RemoveFavorite -> mRemoveHolder.add(change.filmItem)
            }
        }
    }

    override fun <T : Change> getChanges(changeClass: KClass<T>): Array<FilmItem>? =
        when(changeClass) {
            Change.AddFavorite::class -> mAddHolder.toTypedArray()
            Change.RemoveFavorite::class -> mRemoveHolder.toTypedArray()
            else -> null
        }

    override fun hasChanges(changeClass: KClass<Change>) : Boolean =
        when(changeClass) {
            Change.AddFavorite::class -> mAddHolder.size > 0
            Change.RemoveFavorite::class -> mRemoveHolder.size > 0
            else -> false
        }

}