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
        Habit("Делать домашку", "тщательно", Priority.High, Type.Good, 1, 1),
        Habit("Сделать уборку", "на кухне", Priority.Medium, Type.Good, 2, 1),
        Habit("Покурить", "легонько", Priority.Low, Type.Bad, 3, 1),
        Habit("Выпить винцо", "с друзьями", Priority.Medium, Type.Bad, 1, 7),
        Habit("Посмотреть фильм", "из топ-100", Priority.Low, Type.Good, 1, 7),
        Habit("Прокрастинировать", "это как бы плохо, но в то же время перерыв для мозгов",
            Priority.High, Type.Bad, 1, 1)
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