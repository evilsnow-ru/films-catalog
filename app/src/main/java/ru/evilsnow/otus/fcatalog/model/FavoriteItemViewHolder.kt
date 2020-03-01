package ru.evilsnow.otus.fcatalog.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.R

class FavoriteItemViewHolder(
    itemView: View,
    private val mItemClickListener: FavoriteListItemListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var filmItem: FilmItem
    private val filmPosterIv = itemView.findViewById<ImageView>(R.id.favoriteFilmThumbnail)
    private val filmTitleTv = itemView.findViewById<TextView>(R.id.favoriteFilmTitle)

    fun bind(filmItem: FilmItem) {
        this.filmItem = filmItem
        filmPosterIv.setImageResource(filmItem.image)
        filmTitleTv.text = filmItem.title
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.favoriteTrashButton -> mItemClickListener.onRemoveSelected(view, filmItem)
            else -> mItemClickListener.onFilmSelected(view, filmItem)
        }
    }

}