package ru.evilsnow.otus.fcatalog

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.get
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.model.FilmItemsAdapter
import ru.evilsnow.otus.fcatalog.ui.ExitDialog

class MainActivity : AppCompatActivity() {

    private lateinit var mFilmsDao: FilmsDao
    private var lastSelectedFilmPosition: Int? = null
    private var mConfirmExitDialog: Dialog? = null

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

    override fun onBackPressed() {
        if (mConfirmExitDialog == null) {
            mConfirmExitDialog = ExitDialog(this) { super.onBackPressed() }
        }
        mConfirmExitDialog!!.show()
    }

    private fun shareWithFriends() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Hello. New cool app for download: https://github.com/evilsnow-ru/films-catalog")
        }
        startActivity(Intent.createChooser(shareIntent, "Share with friends"))
    }

}
