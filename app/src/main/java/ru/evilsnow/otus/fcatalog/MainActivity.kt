package ru.evilsnow.otus.fcatalog

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import ru.evilsnow.otus.fcatalog.model.FavoritesController
import ru.evilsnow.otus.fcatalog.model.FilmItem
import ru.evilsnow.otus.fcatalog.model.FilmItemsAdapter
import ru.evilsnow.otus.fcatalog.ui.ExitDialog
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener, FavoritesController {

    private var mConfirmExitDialog: Dialog? = null

    private var mFragmentsMap: MutableMap<Int, Fragment> = HashMap(4)
    private var mLastSelectedFragment: Int = -1

    private var mAddedFavoritesList: MutableList<FilmItem> = ArrayList()
    private var mRemovedFavoritesList: MutableList<FilmItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentsContainer)

        if (fragment == null) {
            val filmsFragment = FilmsFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentsContainer, filmsFragment)
                .commit()

            mFragmentsMap[R.id.navBtnHome] = filmsFragment
            mLastSelectedFragment = R.id.navBtnHome
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (mLastSelectedFragment != item.itemId) {
            when (item.itemId) {
                R.id.navBtnHome -> switchFragment(item.itemId) {FilmsFragment()}
                R.id.navBtnFavorites -> switchFragment(item.itemId) {FavoritesFragment()}
            }
        }
        return true
    }

    private fun <T : Fragment> switchFragment(fragmentId: Int, fragmentFactory: () -> T) {
        var fragment = mFragmentsMap[fragmentId]

        if (fragment == null) {
            fragment = fragmentFactory.invoke()
            mFragmentsMap[fragmentId] = fragment
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentsContainer, fragment)
            .commit()

        mLastSelectedFragment = fragmentId
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

    override fun onAddedToFavorites(film: FilmItem) {
        mAddedFavoritesList.add(film)
    }

    override fun getAddedFilms(cleanData: Boolean): List<FilmItem> {
        if (mAddedFavoritesList.size > 0) {
            val result = ArrayList(mAddedFavoritesList)

            if (cleanData) {
                mAddedFavoritesList.clear()
            }

            return result
        }

        return Collections.emptyList()
    }

    override fun onRemoveFromFavorites(film: FilmItem) {
        mRemovedFavoritesList.add(film)
    }

    override fun getRemovedItems(cleanData: Boolean): List<FilmItem> {
        if (mRemovedFavoritesList.size > 0) {
            val result = ArrayList(mRemovedFavoritesList)

            if (cleanData) {
                mRemovedFavoritesList.clear()
            }

            return result
        }

        return Collections.emptyList()
    }

    override fun onRemoveCancel(cancelledFilm: FilmItem) {
        val it: MutableIterator<FilmItem> = mRemovedFavoritesList.iterator()

        while (it.hasNext()) {
            if (it.next().id == cancelledFilm.id) {
                it.remove()
                break
            }
        }
    }

    private fun processRemovedFavorites(data: Intent) {
        /*if (FavoritesActivity.isHasRemoved(data)) {
            FavoritesActivity.getRemovedFilms(data).forEach {
                mListAdapter.getBindedPosition(it)?.let {pos -> mListAdapter.notifyItemChanged(pos) }
            }
        }*/
    }

}
