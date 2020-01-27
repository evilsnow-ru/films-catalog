package ru.evilsnow.otus.fcatalog

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.model.FilmItem

class MainActivity : AppCompatActivity() {

    private lateinit var mFilmsDao: FilmsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFilmsDao = FilmsDao()
        val filmsList = findViewById<ListView>(R.id.filmsList)
        filmsList.adapter = FilmItemsAdapter(this, mFilmsDao.getData())
    }

    class FilmItemsAdapter(private val mContext: Context, private val filmsArray: Array<FilmItem>) : BaseAdapter() {

        private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = mInflater.inflate(R.layout.film_list_item, parent, false)
            val filmItem = getItem(position)
            view.findViewById<TextView>(R.id.filmTitle).text = filmItem.title
            view.findViewById<ImageView>(R.id.filmTitleThumbnail).setImageResource(android.R.drawable.ic_menu_camera)
            return view
        }

        override fun getItem(position: Int): FilmItem = filmsArray[position]

        override fun getItemId(position: Int): Long = filmsArray[position].id.toLong()

        override fun getCount(): Int = filmsArray.size

    }

}
