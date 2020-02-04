package ru.evilsnow.otus.fcatalog.dao

import ru.evilsnow.otus.fcatalog.R
import ru.evilsnow.otus.fcatalog.model.FilmItem

class FilmsDao {

    private val data: Array<FilmItem> = arrayOf(
        FilmItem(0, "Стемптаун", R.string.stumptown_desc, R.drawable.stumptown),
        FilmItem(1, "Аквамен", R.string.aquaman_desc, R.drawable.aquamen),
        FilmItem(2, "Джон Уик 3", R.string.john_wick_desc, R.drawable.john_wick_3),
        FilmItem(3, "Рождественская история", R.string.christmas_carol_desc, R.drawable.christmas_carol),
        FilmItem(4, "Темная башня", R.string.dark_town_desc, R.drawable.dark_tower),
        FilmItem(5, "Venom", R.string.venom_desc, R.drawable.venom)
    )

    fun getData() : Array<FilmItem> = data

    fun getFilm(id: Long): FilmItem? {
        for (film in data) {
            if (film.id.toLong() == id) {
                return film
            }
        }
        return null
    }

}