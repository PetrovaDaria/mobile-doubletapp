package com.example.habittracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.fragments.EditHabitFragment
import com.example.habittracker.R


class EditHabitActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_habit_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.edit_habit_activity,
                    EditHabitFragment()
                )
                .commit()
        }
    }
}