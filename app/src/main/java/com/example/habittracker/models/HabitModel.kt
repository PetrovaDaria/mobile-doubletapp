package com.example.habittracker.models

import com.example.habittracker.Habit
import com.example.habittracker.enums.Priority
import com.example.habittracker.enums.Type

class HabitModel private constructor() {
    companion object {
        private lateinit var instance: HabitModel

        fun getInstance(): HabitModel {
            if (!::instance.isInitialized) {
                instance = HabitModel()
            }
            return instance
        }
    }

    private var habits = mutableListOf(
        Habit("habit", "descr", Priority.High, Type.Good, 1, 1)
    )

    fun add(habit: Habit) {
        habits.add(habit)
    }

    fun edit(habit: Habit) {
        val oldHabit = habits.find{it.Id == habit.Id}
        val oldHabitPosition = habits.indexOf(oldHabit)
        habits[oldHabitPosition] = habit
    }

    fun getAll(): MutableList<Habit> {
        return habits
    }
}