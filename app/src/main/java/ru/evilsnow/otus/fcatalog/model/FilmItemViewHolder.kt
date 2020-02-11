package ru.evilsnow.otus.fcatalog.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.R

class FilmItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val thumbIv: ImageView = itemView.findViewById(R.id.filmTitleThumbnail)
    val filmTitleTv: TextView = itemView.findViewById(R.id.filmTitle)
    val favoriteIv: ImageView = itemView.findViewById(R.id.favoriteIcon)

    fun bind(filmItem: FilmItem) {
        thumbIv.setImageResource(filmItem.image)
        filmTitleTv.text = filmItem.title
        favoriteIv.setImageResource(getFavoriteResource(filmItem.favorite))
    }

    private fun getFavoriteResource(favorite: Boolean) =
        if (favorite) R.drawable.ic_favorite_24px else R.drawable.ic_favorite_border_24px

}