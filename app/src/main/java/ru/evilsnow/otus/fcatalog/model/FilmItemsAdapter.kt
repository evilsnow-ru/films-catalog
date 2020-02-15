package ru.evilsnow.otus.fcatalog.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.R

class FilmItemsAdapter(
    context: Context,
    private val filmsList: List<FilmItem>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<FilmItemViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmItemViewHolder {
        val view = mInflater.inflate(R.layout.film_list_item, parent, false)
        val holder = FilmItemViewHolder(view, itemClickListener)
        view.setOnClickListener(holder)
        view.findViewById<ImageView>(R.id.favoriteIcon).setOnClickListener(holder)
        return holder
    }

    override fun getItemCount(): Int = filmsList.size

    override fun onBindViewHolder(holder: FilmItemViewHolder, position: Int) {
        holder.bind(filmsList[position], position)
    }

    fun getFilmItem(position: Int): FilmItem = filmsList[position]

}