package ru.evilsnow.otus.fcatalog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.model.FavoriteItemsAdapter
import ru.evilsnow.otus.fcatalog.model.FilmItem
import ru.evilsnow.otus.fcatalog.model.ItemClickListener

class FavoritesActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var mFilmsDao: FilmsDao
    private lateinit var mListAdapter: FavoriteItemsAdapter
    private var confirmDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }

        mFilmsDao = FilmsDao.getInstance()
        mListAdapter = FavoriteItemsAdapter(this, mFilmsDao.getFavorites(), this)
        val listLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        findViewById<RecyclerView>(R.id.favoriteFilmsList).apply {
            layoutManager = listLayoutManager
            adapter = mListAdapter
            addItemDecoration(DividerItemDecoration(this@FavoritesActivity, listLayoutManager.orientation))
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

    override fun onItemClicked(view: View, position: Int) {
        val filmItem = mListAdapter.getFilmItem(position)

        when(view.id) {
            R.id.favoriteTrashButton -> removeFromFavorites(filmItem, position)
            else -> showFilmDetails(filmItem)
        }
    }

    private fun removeFromFavorites(filmItem: FilmItem, position: Int) {
        filmItem.favorite = false
        mListAdapter.removeItem(position)
    }

    private fun showFilmDetails(filmItem: FilmItem) {
        val intent = DetailsActivity.newIntent(this, filmItem.id.toLong())
        startActivity(intent)
    }

    companion object {

        fun newIntent(packageContext: Context): Intent =
                Intent(packageContext, FavoritesActivity::class.java)

    }

}