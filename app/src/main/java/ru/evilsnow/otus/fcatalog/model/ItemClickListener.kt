package ru.evilsnow.otus.fcatalog.model

import android.view.View

interface ItemClickListener {
    fun onItemClicked(view: View, position: Int)
}