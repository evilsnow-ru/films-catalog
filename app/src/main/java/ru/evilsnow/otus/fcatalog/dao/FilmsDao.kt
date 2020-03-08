package ru.evilsnow.otus.fcatalog.dao

import ru.evilsnow.otus.fcatalog.R
import ru.evilsnow.otus.fcatalog.model.FilmItem

class FilmsDao private constructor() {

    private val data: Array<FilmItem> = arrayOf(
        FilmItem(0, "Стемптаун", R.string.stumptown_desc, R.drawable.stumptown, false),
        FilmItem(1, "Аквамен", R.string.aquaman_desc, R.drawable.aquamen, false),
        FilmItem(2, "Джон Уик 3", R.string.john_wick_desc, R.drawable.john_wick_3, false),
        FilmItem(3, "Рождественская история", R.string.christmas_carol_desc, R.drawable.christmas_carol, false),
        FilmItem(4, "Темная башня", R.string.dark_town_desc, R.drawable.dark_tower, false),
        FilmItem(5, "Venom", R.string.venom_desc, R.drawable.venom, false)
    )

    fun getData(): List<FilmItem> = data.asList()

    fun getFavorites(): MutableList<FilmItem> = data.asSequence().filter(FilmItem::favorite).toMutableList()

    fun getFilm(id: Long): FilmItem? {
        for (film in data) {
            if (film.id.toLong() == id) {
                return film
            }
        }
        return null
    }

    fun removeFromFavorites(film: FilmItem) {
        film.favorite = false
    }

    fun addToFavorites(film: FilmItem) {
        film.favorite = true
    }

    companion object {

        private val INSTANCE: FilmsDao = FilmsDao()

        fun getInstance(): FilmsDao = INSTANCE

    }

}