package ru.evilsnow.otus.fcatalog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.evilsnow.otus.fcatalog.dao.FilmsDao

class DetailsActivity : AppCompatActivity() {

    private lateinit var mFilmDao: FilmsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)

        val toolbar = findViewById<Toolbar>(R.id.filmDetailsToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }

        mFilmDao = FilmsDao.getInstance()
        val filmId = intent.getLongExtra(FILM_ID_PARAM, -1L)
        mFilmDao.getFilm(filmId)?.let {
            supportActionBar!!.title = it.title
            findViewById<ImageView>(R.id.filmDetailsImg).setImageResource(it.image)
            findViewById<TextView>(R.id.filmDetailsDesc).setText(it.description)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        @JvmField
        val FILM_ID_PARAM = "ru.evilsnow.otus.FILM_ID"

        fun newIntent(packageContext: Context, filmId: Long): Intent {
            val intent = Intent(packageContext, DetailsActivity::class.java)
            intent.putExtra(FILM_ID_PARAM, filmId)
            return intent
        }

    }

}