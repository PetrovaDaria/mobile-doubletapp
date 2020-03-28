package com.example.habittracker.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.habittracker.Habit
import com.example.habittracker.R
import com.example.habittracker.fragments.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity(), ListCallback, EditHabitCallback, NavigationView.OnNavigationItemSelectedListener {
    private val LIST_TAG = "ListFragment"
    private val EDIT_HABIT_TAG = "EditHabitFragment"
    private val ABOUT_TAG = "About Fragment"
    private val viewPagerFragment = ViewPagerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView = findViewById<NavigationView>(R.id.navigation_drawer)
        navView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            replaceFragment(viewPagerFragment, LIST_TAG)
        }
    }

    override fun onAddHabit() {
        replaceFragment(EditHabitFragment(), EDIT_HABIT_TAG)
    }

    override fun onEditHabit(habit: Habit, habitPosition: Int) {
        val fragment = EditHabitFragment.newInstance(habit, habitPosition)
        replaceFragment(fragment, EDIT_HABIT_TAG)
    }

    override fun onSaveHabit(habit: Habit, habitPosition: Int) {
        val bundle = Bundle()
        bundle.putInt("habitPosition", habitPosition)
        bundle.putParcelable("habit", habit)
        viewPagerFragment.arguments = bundle
        onBackPressed()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val fragment = selectDrawerItem(p0.itemId)
        val tag = selectDrawerItemTag(p0.itemId)
        replaceFragment(fragment, tag)
        main_activity.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun selectDrawerItem(itemId: Int): Fragment {
        return when (itemId) {
            R.id.menu_item_main -> viewPagerFragment
            R.id.menu_item_about -> AboutFragment()
            else -> AboutFragment()
        }
    }

    private fun selectDrawerItemTag(itemId: Int): String {
        return when (itemId) {
            R.id.menu_item_main -> LIST_TAG
            R.id.menu_item_about -> ABOUT_TAG
            else -> ABOUT_TAG
        }
    }
}