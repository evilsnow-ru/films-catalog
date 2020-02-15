package ru.evilsnow.otus.fcatalog.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.R

class FavoriteItemViewHolder(
    itemView: View,
    private val itemClickListener: ItemClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val filmPosterIv = itemView.findViewById<ImageView>(R.id.favoriteFilmThumbnail)
    private val filmTitleTv = itemView.findViewById<TextView>(R.id.favoriteFilmTitle)

    fun bind(filmItem: FilmItem) {
        filmPosterIv.setImageResource(filmItem.image)
        filmTitleTv.text = filmItem.title
    }

    override fun onClick(view: View?) {
        itemClickListener.onItemClicked(view!!, adapterPosition)
    }

}