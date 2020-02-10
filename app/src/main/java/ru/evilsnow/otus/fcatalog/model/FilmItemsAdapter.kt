package ru.evilsnow.otus.fcatalog.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ru.evilsnow.otus.fcatalog.R

class FilmItemsAdapter(
    private val mContext: Context,
    private val filmsArray: Array<FilmItem>,
    private val mSelectedIndex: Int?,
    private val detailsCallback: (Int, Int) -> Unit
) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = mInflater.inflate(R.layout.film_list_item, parent, false)
        val filmItem = getItem(position)

        val titleView = view.findViewById<TextView>(R.id.filmTitle)
        titleView.text = filmItem.title

        mSelectedIndex?.let {
            if (it == position) {
                titleView.setTextColor(mContext.getColor(R.color.selectedTitle))
            }
        }

        view.findViewById<ImageView>(R.id.filmTitleThumbnail).setImageResource(filmItem.image)

        view.findViewById<Button>(R.id.filmDetailsBtn).setOnClickListener {
            titleView.setTextColor(mContext.getColor(R.color.selectedTitle))
            detailsCallback.invoke(position, filmItem.id)
        }

        return view
    }

    override fun getItem(position: Int): FilmItem = filmsArray[position]

    override fun getItemId(position: Int): Long = filmsArray[position].id.toLong()

    override fun getCount(): Int = filmsArray.size

}