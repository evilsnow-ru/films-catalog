package ru.evilsnow.otus.fcatalog

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import ru.evilsnow.otus.fcatalog.event.FavoriteRemoveAware
import ru.evilsnow.otus.fcatalog.event.SimpleFavoriteRemoveController
import ru.evilsnow.otus.fcatalog.ui.ExitDialog
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener, FavoriteRemoveAware {

    private var mConfirmExitDialog: Dialog? = null

    private var mFragmentsMap: MutableMap<Int, Fragment> = HashMap(4)
    private var mLastSelectedFragment: Int = -1
    private val mFavoriteRemoveControllerDelegate: FavoriteRemoveAware = SimpleFavoriteRemoveController()

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
                R.id.navBtnHome -> switchFragment(item.itemId) { FilmsFragment() }
                R.id.navBtnFavorites -> switchFragment(item.itemId) { FavoritesFragment() }
                R.id.navBtnAppInfo -> switchFragment(item.itemId) { InfoFragment() }
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

    override fun onBackPressed() {
        if (mConfirmExitDialog == null) {
            mConfirmExitDialog = ExitDialog(this) { super.onBackPressed() }
        }
        mConfirmExitDialog!!.show()
    }

    override fun onRemove(filmId: Int) {
        mFavoriteRemoveControllerDelegate.onRemove(filmId)
    }

    override fun getRemovedFavoriteFilms(): Array<Int> = mFavoriteRemoveControllerDelegate.getRemovedFavoriteFilms()

}
