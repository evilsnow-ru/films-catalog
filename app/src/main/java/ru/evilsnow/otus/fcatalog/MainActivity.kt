package ru.evilsnow.otus.fcatalog

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import ru.evilsnow.otus.fcatalog.model.FilmItem
import ru.evilsnow.otus.fcatalog.model.FilmItemsAdapter
import ru.evilsnow.otus.fcatalog.model.ItemClickListener
import ru.evilsnow.otus.fcatalog.ui.ExitDialog


class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener, ItemClickListener {

    private lateinit var mListAdapter: FilmItemsAdapter
    private var mConfirmExitDialog: Dialog? = null
    private var mFragmentsMap: MutableMap<Int, Fragment> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentsContainer)

        if (fragment == null) {
            val filmsFragment = FilmsFragment()
            mFragmentsMap[R.id.navBtnHome] = filmsFragment

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentsContainer, filmsFragment)
                .commit()
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("MYAPP", "Selected: ${item.itemId}")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {

                R.id.favorites_menu_item -> {
                    startActivityForResult(FavoritesActivity.newIntent(this), AR_MANAGE_FAVORITES_FILMS)
                    return true
                }

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

    override fun onItemClicked(view: View, position: Int) {
        val filmItem = mListAdapter.getFilmItem(position)

        when(view.id) {
            R.id.favoriteIcon -> onFavoriteClicked(view, filmItem)
            else -> showFilmDetails(filmItem)
        }
    }

    private fun showFilmDetails(filmItem: FilmItem) {
        val intent = DetailsActivity.newIntent(this, filmItem.id.toLong())
        startActivity(intent)
    }

    private fun onFavoriteClicked(view: View, filmItem: FilmItem) {
        val iconResourceId: Int

        if (filmItem.favorite) {
            filmItem.favorite = false
            iconResourceId = R.drawable.ic_favorite_border_24px
        } else {
            filmItem.favorite = true
            iconResourceId = R.drawable.ic_favorite_24px
        }

        if (view is ImageView) {
            view.setImageDrawable(ContextCompat.getDrawable(this, iconResourceId))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            AR_MANAGE_FAVORITES_FILMS -> processRemovedFavorites(data!!)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processRemovedFavorites(data: Intent) {
        if (FavoritesActivity.isHasRemoved(data)) {
            FavoritesActivity.getRemovedFilms(data).forEach {
                mListAdapter.getBindedPosition(it)?.let {pos -> mListAdapter.notifyItemChanged(pos) }
            }
        }
    }

    companion object {
        private const val AR_MANAGE_FAVORITES_FILMS: Int = 1
    }

}
