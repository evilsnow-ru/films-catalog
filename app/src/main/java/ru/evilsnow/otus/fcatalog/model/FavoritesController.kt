package ru.evilsnow.otus.fcatalog.model

interface FavoritesController {

    fun onAddedToFavorites(film: FilmItem)
    fun getAddedFilms(cleanData: Boolean = true): List<FilmItem>
    fun onRemoveFromFavorites(film: FilmItem)
    fun getRemovedItems(cleanData: Boolean = true): List<FilmItem>
    fun onRemoveCancel(cancelledFilm: FilmItem)

}