package ru.evilsnow.otus.fcatalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.event.FavoriteRemoveAware
import ru.evilsnow.otus.fcatalog.model.FavoriteItemsAdapter
import ru.evilsnow.otus.fcatalog.model.FavoriteListItemListener
import ru.evilsnow.otus.fcatalog.model.FilmItem

class FavoritesFragment : Fragment(), FavoriteListItemListener {

    private val TAG = Consts.APP_ROOT_LOG_TAG + "." + javaClass.simpleName

    private val mFilmsList: MutableList<FilmItem> = ArrayList()
    private val mChangeSet: MutableSet<FilmItem> = HashSet()
    private var mRemoveListener: FavoriteRemoveAware? = null

    private lateinit var mFilmsDao: FilmsDao
    private lateinit var mListAdapter: FavoriteItemsAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFilmsDao = FilmsDao.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val items = mFilmsDao.getFavorites()

        if (items.isNotEmpty()) {
            mFilmsList.clear()
            mFilmsList.addAll(items)
            mListAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is FavoriteRemoveAware) {
            mRemoveListener = activity as FavoriteRemoveAware
        } else {
            Log.w(TAG, "Activity must implements FavoriteRemoveAware to propagate remove events")
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
            mChangeSet.add(filmItem)
            mFilmsDao.removeFromFavorites(filmItem)

            val snackBar = Snackbar.make(view, R.string.removed, Snackbar.LENGTH_SHORT)

            snackBar.setAction(R.string.cancel) {
                mChangeSet.remove(filmItem)
                mFilmsList.add(filmItem)
                mFilmsDao.addToFavorites(filmItem)
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

    override fun onStop() {
        super.onStop()

        if (mChangeSet.size > 0) {
            mRemoveListener?.let {listener ->
                mChangeSet
                    .asSequence()
                    .map { filmItem -> filmItem.id }
                    .forEach { listener.onRemove(it) }
            }

            mChangeSet.clear()
        }
    }

}