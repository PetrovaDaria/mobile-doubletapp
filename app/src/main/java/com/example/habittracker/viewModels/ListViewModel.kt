package com.example.habittracker.viewModels

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.habittracker.Habit
import com.example.habittracker.HabitDatabase
import com.example.habittracker.enums.PrioritySort
import com.example.habittracker.enums.Type
import java.util.*

class ListViewModel(private val lifecycleOwner: LifecycleOwner, private val db: HabitDatabase, private val habitType: Type): ViewModel() {
    private val mutableHabits: MutableLiveData<List<Habit>> = MutableLiveData()

    val habits: LiveData<List<Habit>> = mutableHabits

    private var sequence = MutableLiveData<String>().apply { value = "" }
    private var prioritySort = MutableLiveData<PrioritySort>().apply { value = PrioritySort.None }

    init {
        sequence.observe(lifecycleOwner, Observer { _ ->
            applySettings()
        })

        prioritySort.observe(lifecycleOwner, Observer { _ ->
            applySettings()
        })
    }

    fun setSearchFilter(sequence: String) {
        this.sequence.value = sequence.toLowerCase(Locale.ROOT)
        applySettings()
    }

    fun setPrioritySort(prioritySort: PrioritySort) {
        this.prioritySort.value = prioritySort
        applySettings()
    }

    private fun applySettings() {
        var filteredHabits = db.habitDao().getAll()
        filteredHabits = filterByType(filteredHabits)
        filteredHabits = filterBySequence(filteredHabits)
        filteredHabits = sortByPriority(filteredHabits)
        mutableHabits.value = filteredHabits

    }

    private fun filterByType(habits: List<Habit>): List<Habit> {
        return habits.filter{it.Type == habitType}
    }

    private fun filterBySequence(habits: List<Habit>): List<Habit> {
        if (this.sequence.value != "") {
            return habits.filter {
                it.Name.toLowerCase(Locale.ROOT).contains(this.sequence.value!!) or
                        it.Description.toLowerCase(Locale.ROOT).contains(this.sequence.value!!)
            }
        }
        return habits
    }

    private fun sortByPriority(habits: List<Habit>): List<Habit> {
        if (prioritySort.value == PrioritySort.HighToLow) {
            return habits.sortedByDescending {it.Priority}
        }
        if (prioritySort.value == PrioritySort.LowToHigh) {
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