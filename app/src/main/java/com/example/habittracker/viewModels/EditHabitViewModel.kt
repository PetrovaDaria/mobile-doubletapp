package com.example.habittracker.viewModels

import androidx.lifecycle.ViewModel
import com.example.habittracker.Habit
import com.example.habittracker.models.HabitModel
import java.util.*

class EditHabitViewModel(private val model: HabitModel): ViewModel() {
    fun addHabit(habit: Habit) {
        model.add(habit)
    }

    fun editHabit(habit: Habit) {
        model.edit(habit)
    }

    fun getHabitById(id: UUID): Habit? {
        return model.getAll().find {it.Id == id}
    }
}