package ru.evilsnow.otus.fcatalog.model

import android.view.View

interface FilmListItemListener {

    fun onFilmSelected(view: View, filmItem: FilmItem)
    fun onFavoriteSelected(view: View, filmItem: FilmItem)

}