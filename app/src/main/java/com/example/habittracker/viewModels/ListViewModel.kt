package com.example.habittracker.viewModels

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.habittracker.models.Habit
import com.example.habittracker.db.HabitDatabase
import com.example.habittracker.enums.PrioritySort
import com.example.habittracker.enums.Type
import java.util.*

class ListViewModel(private val db: HabitDatabase, private val habitType: Type): ViewModel() {
    private lateinit var allHabits: List<Habit>
    private val getAllObserver = Observer<List<Habit>> { habits ->
        allHabits = habits
        applySettings(habits)
    }
    private val mutableHabits: MutableLiveData<List<Habit>> = MutableLiveData()

    val habits: LiveData<List<Habit>> = mutableHabits

    private var sequence = ""
    private var prioritySort = PrioritySort.None

    init {
        db.habitDao().getAll().observeForever(getAllObserver)
    }

    override fun onCleared() {
        super.onCleared()
        db.habitDao().getAll().removeObserver(getAllObserver)
    }

    fun setSearchFilter(sequence: String) {
        this.sequence = sequence.toLowerCase(Locale.ROOT)
        applySettings(allHabits)
    }

    fun setPrioritySort(prioritySort: PrioritySort) {
        this.prioritySort = prioritySort
        applySettings(allHabits)
    }

    private fun applySettings(habits: List<Habit>) {
        var filteredHabits = filterByType(habits)
        filteredHabits = filterBySequence(filteredHabits)
        filteredHabits = sortByPriority(filteredHabits)
        mutableHabits.value = filteredHabits
    }

    private fun filterByType(habits: List<Habit>): List<Habit> {
        return habits.filter{it.Type == habitType}
    }

    private fun filterBySequence(habits: List<Habit>): List<Habit> {
        if (this.sequence != "") {
            return habits.filter {
                it.Name.toLowerCase(Locale.ROOT).contains(this.sequence) or
                        it.Description.toLowerCase(Locale.ROOT).contains(this.sequence)
            }
        }
        return habits
    }

    private fun sortByPriority(habits: List<Habit>): List<Habit> {
        if (prioritySort == PrioritySort.HighToLow) {
            return habits.sortedByDescending {it.Priority}
        }
        if (prioritySort == PrioritySort.LowToHigh) {
            return habits.sortedBy {it.Priority}
        }

        return habits
    }

//    private fun applySettings() {
//        if (sequence.value == "") {
//            when (prioritySort.value) {
//                PrioritySort.None -> {
//                    mutableHabits.value = db.habitDao().filterByType(habitType)
//                }
//                PrioritySort.HighToLow -> {
//                    mutableHabits.value = db.habitDao().filterByTypeAndSortDescByPriority(habitType)
//                }
//                PrioritySort.LowToHigh -> {
//                    mutableHabits.value = db.habitDao().filterByTypeAndSortAscByPriority(habitType)
//                }
//            }
//        } else {
//            when (prioritySort.value) {
//                PrioritySort.None -> {
//                    mutableHabits.value = db.habitDao().filterByTypeAndSearch(habitType, sequence.value!!)
//                }
//                PrioritySort.HighToLow -> {
//                    mutableHabits.value = db.habitDao()
//                        .filterByTypeAndSearchAndSortByDescPriority(habitType, sequence.value!!)
//                }
//                PrioritySort.LowToHigh -> {
//                    mutableHabits.value = db.habitDao().filterByTypeAndSearchAndSortByAscPriority(habitType, sequence.value!!)
//
//                }
//            }
//        }
//    }

//    private fun formulateQuery(): String {
//        var query = "SELECT * FROM WHERE type = $habitType"
//        if (sequence.value != "") {
//            query += " AND (name LIKE $sequence OR description LIKE $sequence"
//        }
//        if (prioritySort.value != PrioritySort.None) {
//            query += " ORDER BY priority"
//            query += if (prioritySort.value == PrioritySort.HighToLow) {
//                " DESC"
//            } else {
//                " ASC"
//            }
//        }
//        return query
//    }
}