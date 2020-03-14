package ru.evilsnow.otus.fcatalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.event.FavoriteRemoveAware
import ru.evilsnow.otus.fcatalog.model.FilmItem
import ru.evilsnow.otus.fcatalog.model.FilmItemsAdapter
import ru.evilsnow.otus.fcatalog.model.FilmListItemListener

class FilmsFragment : Fragment(), FilmListItemListener {

    private val TAG = Consts.APP_ROOT_LOG_TAG + "." + javaClass.simpleName

    private val mFilmsList: MutableList<FilmItem> = ArrayList()
    private var isFirstRun: Boolean = true
    private var mFavoriteRemoveController: FavoriteRemoveAware? = null

    private lateinit var mFilmsDao: FilmsDao
    private lateinit var mFilmsAdapter: FilmItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val listLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val filmsList = view.findViewById<RecyclerView>(R.id.filmsList)
        mFilmsAdapter = FilmItemsAdapter(context!!, mFilmsList,this)

        filmsList.apply {
            layoutManager = listLayoutManager
            addItemDecoration(DividerItemDecoration(context, listLayoutManager.orientation))
            adapter = mFilmsAdapter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFilmsDao = FilmsDao.getInstance()
    }

    override fun onStart() {
        super.onStart()

        if (isFirstRun) {
            val items = mFilmsDao.getData()

            if (items.isNotEmpty()) {
                mFilmsList.addAll(items)
                mFilmsAdapter.notifyItemRangeInserted(0, items.size)
            }

            isFirstRun = false
        } else {
            mFavoriteRemoveController?.let { favoriteController ->
                val changes = favoriteController.getRemovedFavoriteFilms()
                if (changes.isNotEmpty()) {
                    val changeSet = changes.toSet()
                    val it = mFilmsList.listIterator().withIndex()
                    while (it.hasNext()) {
                        var filmItem = it.next()
                        if (changeSet.contains(filmItem.value.id)) {
                            mFilmsAdapter.notifyItemChanged(filmItem.index)
                        }
                    }
                }
            }
        }
    }

    override fun onFilmSelected(view: View, filmItem: FilmItem) {
        val intent = DetailsActivity.newIntent(context!!, filmItem.id.toLong())
        startActivity(intent)
    }

    override fun onFavoriteSelected(view: View, filmItem: FilmItem) {
        val iconResourceId: Int

        if (filmItem.favorite) {
            mFilmsDao.removeFromFavorites(filmItem)
            iconResourceId = R.drawable.ic_favorite_border_24px
            Toast.makeText(context, R.string.removed, Toast.LENGTH_SHORT).show()
        } else {
            filmItem.favorite = true
            mFilmsDao.addToFavorites(filmItem)
            iconResourceId = R.drawable.ic_favorite_24px
            Toast.makeText(context, R.string.added, Toast.LENGTH_SHORT).show()
        }

        if (view is ImageView) {
            view.setImageDrawable(ContextCompat.getDrawable(context!!, iconResourceId))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is FavoriteRemoveAware) {
            mFavoriteRemoveController = activity as FavoriteRemoveAware
        } else {
            Log.w(TAG, "Activity must implements FavoriteRemoveAware to propagate remove events")
        }
    }

}
