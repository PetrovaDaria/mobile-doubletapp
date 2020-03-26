package com.example.habittracker.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
    private val viewPagerFragment = ViewPagerFragment()

    // private lateinit var myToolbar: Toolbar
    // private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // myToolbar = toolbar
        // setSupportActionBar(toolbar)

//        drawerToggle = ActionBarDrawerToggle(this, main_activity, toolbar, R.string.open_drawer, R.string.close_drawer)
//        drawerToggle.isDrawerIndicatorEnabled = true
//        main_activity.addDrawerListener(drawerToggle)
//        drawerToggle.syncState()
//        navigation_drawer.setNavigationItemSelectedListener(this)
//        navigation_drawer.bringToFront()

        val navView = findViewById<NavigationView>(R.id.navigation_drawer)
        navView.setNavigationItemSelectedListener(this)
        navView.bringToFront()

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_activity,
                    viewPagerFragment,
                    LIST_TAG
                )
                .addToBackStack(LIST_TAG)
                .commit()
        }
    }

    override fun onAddHabit() {
        println("add")
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.main_activity,
                EditHabitFragment(),
                EDIT_HABIT_TAG
            )
            .addToBackStack(EDIT_HABIT_TAG)
            .commit()
    }

    override fun onEditHabit(habit: Habit, habitPosition: Int) {
        println("edit")
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.main_activity,
                EditHabitFragment.newInstance(habit, habitPosition),
                EDIT_HABIT_TAG
            )
            .addToBackStack(EDIT_HABIT_TAG)
            .commit()
    }

    override fun onSaveHabit(habit: Habit, habitPosition: Int) {
        println("save")
        val bundle = Bundle()
        bundle.putInt("habitPosition", habitPosition)
        bundle.putParcelable("habit", habit)
        viewPagerFragment.arguments = bundle
        onBackPressed()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        println("click")
        val fragment = selectDrawerItem(p0.itemId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_activity, fragment)
            .commit()
        return true
    }

    private fun selectDrawerItem(itemId: Int): Fragment {
        return when (itemId) {
            R.id.nav_list -> viewPagerFragment
            R.id.nav_about -> AboutFragment()
            else -> AboutFragment()
        }
    }
}