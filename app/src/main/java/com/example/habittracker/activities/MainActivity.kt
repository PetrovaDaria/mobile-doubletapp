package com.example.habittracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.habittracker.fragments.ListFragment
import com.example.habittracker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.main_activity,
                    ListFragment()
                )
                .commit()
        }
    }
}
