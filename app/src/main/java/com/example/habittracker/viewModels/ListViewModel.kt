package com.example.habittracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.Habit
import com.example.habittracker.enums.Type
import com.example.habittracker.models.HabitModel

class ListViewModel(private val model: HabitModel, private val habitType: Type): ViewModel() {
    private val mutableHabits: MutableLiveData<List<Habit>> = MutableLiveData()

    val habits: LiveData<List<Habit>> = mutableHabits

    init {
        getHabits()
    }

    fun getHabits() {
        val allHabits = model.getAll()
        val filteredHabits = allHabits.filter{it.Type == habitType}
        mutableHabits.value = filteredHabits as MutableList<Habit>
    }


}