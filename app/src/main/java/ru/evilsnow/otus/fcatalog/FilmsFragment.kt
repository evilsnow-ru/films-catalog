package ru.evilsnow.otus.fcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.evilsnow.otus.fcatalog.dao.FilmsDao
import ru.evilsnow.otus.fcatalog.model.FilmItem
import ru.evilsnow.otus.fcatalog.model.FilmItemsAdapter
import ru.evilsnow.otus.fcatalog.model.ItemClickListener

class FilmsFragment : Fragment(), ItemClickListener {

    private val mFilmsList: MutableList<FilmItem> = ArrayList()
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

    override fun onStart() {
        super.onStart()
        val items = FilmsDao.getInstance().getData()

        if (items.isNotEmpty()) {
            mFilmsList.addAll(items)
            mFilmsAdapter.notifyItemRangeInserted(0, items.size)
        }
    }

    override fun onItemClicked(view: View, position: Int) {

    }

}
