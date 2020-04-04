package com.example.habittracker.models

import android.os.Parcelable
import androidx.room.*
import com.example.habittracker.enums.Priority
import com.example.habittracker.enums.Type
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "habits")
@TypeConverters(PriorityConverter::class, HabitTypeConverter::class)
class Habit(
    @ColumnInfo(name="name")
    var Name: String,
    @ColumnInfo(name="description")
    var Description: String,
    @ColumnInfo(name="priority")
    var Priority: Priority,
    @ColumnInfo(name="type")
    var Type: Type,
    @ColumnInfo(name="times")
    var Times: Int,
    @ColumnInfo(name="period")
    var Period: Int,
    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    val Id: Int? = null
): Parcelable {
}

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: Priority): Int {
        return priority.ordinal;
    }

    @TypeConverter
    fun toPriority(priorityIndex: Int): Priority {
        return Priority.values()[priorityIndex];
    }
}

class HabitTypeConverter {
    @TypeConverter
    fun fromType(type: Type): Int {
        return type.ordinal;
    }

    @TypeConverter
    fun toType(typeIndex: Int): Type {
        return Type.values()[typeIndex];
    }
}