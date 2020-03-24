package com.example.habittracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.habittracker.Habit
import com.example.habittracker.fragments.ListFragment
import com.example.habittracker.R
import com.example.habittracker.fragments.EditHabitCallback
import com.example.habittracker.fragments.EditHabitFragment
import com.example.habittracker.fragments.ListCallback

class MainActivity : AppCompatActivity(), ListCallback, EditHabitCallback {
    val LIST_TAG = "ListFragment"
    val EDIT_HABIT_TAG = "EditHabitFragment"
    val listFragment = ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_activity,
                    listFragment,
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

    override fun onBackPressed() {
        super.onBackPressed()

        println("on back pressed")
    }

    override fun onSaveHabit(habit: Habit, habitPosition: Int) {
        println("save")
        val bundle = Bundle()
        bundle.putInt("habitPosition", habitPosition)
        bundle.putParcelable("habit", habit)
        listFragment.arguments = bundle
        onBackPressed()
    }
}
