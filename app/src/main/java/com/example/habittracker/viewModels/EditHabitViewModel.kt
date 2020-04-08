package com.example.habittracker.viewModels

import androidx.lifecycle.ViewModel
import com.example.habittracker.models.Habit
import com.example.habittracker.db.HabitDatabase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class EditHabitViewModel(private val db: HabitDatabase): ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler{_, e -> throw e}

    fun addHabit(habit: Habit) {
        launch {
            db.habitDao().insert(habit)
        }
    }

    fun editHabit(habit: Habit) {
        launch {
            db.habitDao().update(habit)
        }
    }

    fun getHabitById(id: Int): Habit {
        return db.habitDao().getHabitById(id);
    }
}