package ru.evilsnow.otus.fcatalog.model

data class FilmItem (
    val id: Int,
    val title: String,
    val description: Int,
    val image: Int,
    var favorite: Boolean
)