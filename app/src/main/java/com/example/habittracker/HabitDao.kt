package com.example.habittracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.habittracker.enums.Type

@Dao
interface HabitDao {
    @Query("SELECT * from habits")
    fun getAll(): List<Habit>

//    @Query("SELECT * FROM habits WHERE type = (:type)")
//    fun filterByType(type: Int): List<Habit>
//
//    @Query("SELECT * FROM habits WHERE type = (:type) and (name LIKE (:sequence) or description LIKE (:sequence))")
//    fun filterByTypeAndSearch(type: Type, sequence: String): List<Habit>
//
//    @Query("SELECT * FROM habits WHERE type = (:type) ORDER BY priority ASC")
//    fun filterByTypeAndSortAscByPriority(type: Type): List<Habit>
//
//    @Query("SELECT * FROM habits WHERE type = (:type) ORDER BY priority DESC")
//    fun filterByTypeAndSortDescByPriority(type: Type): List<Habit>
//
//    @Query("SELECT * FROM habits WHERE type = (:type) and (name LIKE (:sequence) or description LIKE (:sequence)) ORDER BY priority ASC")
//    fun filterByTypeAndSearchAndSortByAscPriority(type: Type, sequence: String): List<Habit>
//
//    @Query("SELECT * FROM habits WHERE type = (:type) and (name LIKE (:sequence) or description LIKE (:sequence)) ORDER BY priority DESC")
//    fun filterByTypeAndSearchAndSortByDescPriority(type: Type, sequence: String): List<Habit>

    @Query("SELECT * FROM habits WHERE id = (:id) LIMIT 1")
    fun getHabitById(id: Int): Habit

    @Insert
    fun insert(habit: Habit)

    @Update
    fun update(habit: Habit)
}