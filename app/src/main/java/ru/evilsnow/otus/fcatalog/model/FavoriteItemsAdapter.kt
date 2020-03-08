package ru.evilsnow.otus.fcatalog.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.R

class FavoriteItemsAdapter(
    context: Context,
    private val mItems: MutableList<FilmItem>,
    private val itemClickListener: FavoriteListItemListener
) : RecyclerView.Adapter<FavoriteItemViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteItemViewHolder {
        val view = mLayoutInflater.inflate(R.layout.favorite_list_item, parent, false)
        val holder = FavoriteItemViewHolder(view, itemClickListener)
        view.setOnClickListener(holder)
        view.findViewById<ImageView>(R.id.favoriteTrashButton).setOnClickListener(holder)
        return holder
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: FavoriteItemViewHolder, position: Int) {
        val filmItem = mItems[position]
        holder.bind(filmItem)
    }

}