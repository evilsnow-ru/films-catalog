package ru.evilsnow.otus.fcatalog.dao

import ru.evilsnow.otus.fcatalog.model.FilmItem

class FilmsDao {

    private val data: Array<FilmItem> = arrayOf(
        FilmItem(0, "Title1"),
        FilmItem(1, "Title2"),
        FilmItem(2, "Title3"),
        FilmItem(3, "Title4"),
        FilmItem(4, "Title5"),
        FilmItem(5, "Title6")
    )

    fun getData() : Array<FilmItem> = data

}