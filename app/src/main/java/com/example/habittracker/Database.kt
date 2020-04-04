package com.example.habittracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1)
abstract class HabitDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        private lateinit var instance: HabitDatabase

        fun getInstance(context: Context): HabitDatabase {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context,
                    HabitDatabase::class.java,
                    "habitsDB")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}