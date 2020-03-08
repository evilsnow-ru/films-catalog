package ru.evilsnow.otus.fcatalog.event

interface FavoriteRemoveAware {
    fun onRemove(filmId: Int)
    fun getRemovedFavoriteFilms(): Array<Int>
}