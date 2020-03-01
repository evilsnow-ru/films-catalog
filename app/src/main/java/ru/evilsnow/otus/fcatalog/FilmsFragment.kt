package ru.evilsnow.otus.fcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.model.FavoritesController
import ru.evilsnow.otus.fcatalog.model.FilmItem
import ru.evilsnow.otus.fcatalog.model.FilmItemsAdapter
import ru.evilsnow.otus.fcatalog.model.FilmListItemListener
import java.lang.IllegalStateException

class FilmsFragment : Fragment(), FilmListItemListener {

    private val mFilmsList: MutableList<FilmItem> = ArrayList()
    private lateinit var mFilmsAdapter: FilmItemsAdapter
    private lateinit var mFavoritesController: FavoritesController

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

    override fun onStart() {
        super.onStart()
        val items = FilmsDao.getInstance().getData()

        if (items.isNotEmpty()) {
            mFilmsList.addAll(items)
            mFilmsAdapter.notifyItemRangeInserted(0, items.size)
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

    override fun onFavoriteSelected(view: View, filmItem: FilmItem) {
        val iconResourceId: Int

        if (filmItem.favorite) {
            filmItem.favorite = false
            iconResourceId = R.drawable.ic_favorite_border_24px
        } else {
            filmItem.favorite = true
            iconResourceId = R.drawable.ic_favorite_24px
        }

        if (view is ImageView) {
            view.setImageDrawable(ContextCompat.getDrawable(context!!, iconResourceId))
        }
    }

}
