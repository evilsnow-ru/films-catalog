package ru.evilsnow.otus.fcatalog

import android.app.Activity
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
    private val mRemovedFavorites: MutableList<Int> = ArrayList()

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

        savedInstanceState?.let {
            mRemovedFavorites.addAll(it.getIntArray(RESULT_FAVORITES_REMOVED)!!.toList())
        }

    }

    override fun onBackPressed() {
        setResponse()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                setResponse()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setResponse() {
        val result = Intent()
        result.putExtra(RESULT_FAVORITES_REMOVED, mRemovedFavorites.toIntArray())
        setResult(Activity.RESULT_OK, result)
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
        mRemovedFavorites.add(filmItem.id)
    }

    private fun showFilmDetails(filmItem: FilmItem) {
        val intent = DetailsActivity.newIntent(this, filmItem.id.toLong())
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(RESULT_FAVORITES_REMOVED, mRemovedFavorites.toIntArray())
    }

    companion object {

        private const val RESULT_FAVORITES_REMOVED = "ru.evilsnow.RESULT_FAVORITES_DELETED"

        fun newIntent(packageContext: Context): Intent =
                Intent(packageContext, FavoritesActivity::class.java)

        fun isHasRemoved(data: Intent): Boolean {
            val removedArray = data.getIntArrayExtra(RESULT_FAVORITES_REMOVED)
            return (removedArray != null && removedArray.isNotEmpty())
        }

        fun getRemovedFilms(data: Intent): IntArray {
            return data.getIntArrayExtra(RESULT_FAVORITES_REMOVED)
        }

    }

}