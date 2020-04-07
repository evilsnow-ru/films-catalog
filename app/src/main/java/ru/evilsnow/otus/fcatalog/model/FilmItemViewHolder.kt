package ru.evilsnow.otus.fcatalog.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.R

class FilmItemViewHolder(
    itemView: View,
    private val mFilmListItemListener: FilmListItemListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val thumbIv: ImageView = itemView.findViewById(R.id.filmTitleThumbnail)
    private val filmTitleTv: TextView = itemView.findViewById(R.id.filmTitle)
    private val filmShortDscTv: TextView = itemView.findViewById(R.id.filmDescriptionShort)
    private val favoriteIv: ImageView = itemView.findViewById(R.id.favoriteIcon)

    private var filmItem: FilmItem? = null

    fun bind(filmItem: FilmItem): FilmItem? {
        thumbIv.setImageResource(filmItem.image)
        filmTitleTv.text = filmItem.title
        filmShortDscTv.setText(filmItem.description)
        favoriteIv.setImageResource(getFavoriteResource(filmItem.favorite))
        val oldFilmItem = this.filmItem
        this.filmItem = filmItem
        return oldFilmItem
    }

    private fun getFavoriteResource(favorite: Boolean) =
        if (favorite) R.drawable.ic_favorite_24px else R.drawable.ic_favorite_border_24px

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.favoriteIcon -> mFilmListItemListener.onFavoriteSelected(v, filmItem!!)
            else -> mFilmListItemListener.onFilmSelected(v, filmItem!!)
        }
    }

}