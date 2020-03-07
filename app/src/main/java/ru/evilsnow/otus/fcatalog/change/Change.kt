package ru.evilsnow.otus.fcatalog.change

import ru.evilsnow.otus.fcatalog.model.FilmItem

sealed class Change {
    data class AddFavorite(val filmItem: FilmItem) : Change()
    data class RemoveFavorite(val filmItem: FilmItem) : Change()
}
