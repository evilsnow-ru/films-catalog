package ru.evilsnow.otus.fcatalog.model

import android.view.View

interface FavoriteListItemListener {

    fun onFilmSelected(view: View, filmItem: FilmItem)
    fun onRemoveSelected(view: View, filmItem: FilmItem)

}