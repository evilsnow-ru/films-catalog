package ru.evilsnow.otus.fcatalog

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.get
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.model.FilmItem

class MainActivity : AppCompatActivity() {

    private lateinit var mFilmsDao: FilmsDao
    private var lastSelectedFilmPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Marks last selected film (yes, position is bad, but at now our data doesn't changes)
        savedInstanceState?.let {
            val selectedPosition = it.getInt(PROP_SELECTED_FILM_POSITION, -1)

            if (selectedPosition >= 0) {
                lastSelectedFilmPosition = selectedPosition
            }
        }

        mFilmsDao = FilmsDao()

        val filmsList = findViewById<ListView>(R.id.filmsList)
        filmsList.adapter = FilmItemsAdapter(this, mFilmsDao.getData(), lastSelectedFilmPosition) {position, filmId ->
            lastSelectedFilmPosition?.let {
                filmsList[it]
                    .findViewById<TextView>(R.id.filmTitle).setTextColor(getColor(R.color.black))
            }

            lastSelectedFilmPosition = position
            val intent = DetailsActivity.newIntent(this@MainActivity, filmId.toLong())
            startActivity(intent)
        }

    }

    companion object {
        const val PROP_SELECTED_FILM_POSITION = "ru.evilsnow.main.SELECTED_POSITION"
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        lastSelectedFilmPosition?.let {
            outState!!.putInt(PROP_SELECTED_FILM_POSITION, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.activity_main_menu, it)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.share_menu_item -> {
                    shareWithFriends()
                    return true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareWithFriends() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Hello. New cool app for download: https://github.com/evilsnow-ru/films-catalog")
        }
        startActivity(Intent.createChooser(shareIntent, "Share with friends"))
    }

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

}
