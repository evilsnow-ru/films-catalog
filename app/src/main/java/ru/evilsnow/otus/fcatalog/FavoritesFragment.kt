package ru.evilsnow.otus.fcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.model.FavoriteItemsAdapter
import ru.evilsnow.otus.fcatalog.model.FavoriteListItemListener
import ru.evilsnow.otus.fcatalog.model.FavoritesController
import ru.evilsnow.otus.fcatalog.model.FilmItem
import java.lang.IllegalStateException

class FavoritesFragment : Fragment(), FavoriteListItemListener {

    private val mFilmsList: MutableList<FilmItem> = ArrayList()
    private lateinit var mListAdapter: FavoriteItemsAdapter
    private lateinit var mFavoritesController: FavoritesController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        mListAdapter = FavoriteItemsAdapter(context!!, mFilmsList, this)
        val listLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        view.findViewById<RecyclerView>(R.id.favoriteFilmsList).apply {
            layoutManager = listLayoutManager
            adapter = mListAdapter
            addItemDecoration(DividerItemDecoration(context, listLayoutManager.orientation))
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val items = FilmsDao.getInstance().getFavorites()

        if (items.isNotEmpty()) {
            mFilmsList.addAll(items)
            mListAdapter.notifyItemRangeInserted(0, items.size)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is FavoritesController) {
            mFavoritesController = activity as FavoritesController
        } else {
            throw IllegalStateException("Activity must implements FavoritesController")
        }
    }

    override fun onFilmSelected(view: View, filmItem: FilmItem) {
        val intent = DetailsActivity.newIntent(context!!, filmItem.id.toLong())
        startActivity(intent)
    }

    override fun onRemoveSelected(view: View, filmItem: FilmItem) {
        val position = removeFromList(filmItem)

        if (position > -1) {
            mListAdapter.notifyItemRemoved(position)
            mFavoritesController.onRemoveFromFavorites(filmItem)
            val snackBar = Snackbar.make(view, R.string.removed, Snackbar.LENGTH_SHORT)

            snackBar.setAction(R.string.cancel) {
                mFavoritesController.onRemoveCancel(filmItem)
                mFilmsList.add(filmItem)
                mListAdapter.notifyItemInserted(mFilmsList.size - 1)
            }

            snackBar.show()
        }
    }

    private fun removeFromList(filmItem: FilmItem): Int {
        val it = mFilmsList.iterator()
        var idx = -1

        while (it.hasNext()) {
            idx++
            if (it.next().id == filmItem.id) {
                it.remove()
                return idx
            }
        }

        return -1
    }

}