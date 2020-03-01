package ru.evilsnow.otus.fcatalog.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.R

class FilmItemsAdapter(
    context: Context,
    private val mFilmsList: List<FilmItem>,
    private val mFilmListItemListener: FilmListItemListener
) : RecyclerView.Adapter<FilmItemViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mBindedItems: MutableMap<Int, Int> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmItemViewHolder {
        val view = mInflater.inflate(R.layout.film_list_item, parent, false)
        val holder = FilmItemViewHolder(view, mFilmListItemListener)
        view.setOnClickListener(holder)
        view.findViewById<ImageView>(R.id.favoriteIcon).setOnClickListener(holder)
        return holder
    }

    override fun getItemCount(): Int = mFilmsList.size

    override fun onBindViewHolder(holder: FilmItemViewHolder, position: Int) {
        val filmItem = mFilmsList[position]
        mBindedItems[filmItem.id] = position
        holder.bind(filmItem)?.let { mBindedItems.remove(it.id) }
    }

    fun getBindedPosition(filmId: Int): Int? {
        return mBindedItems[filmId]
    }

}