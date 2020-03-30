package com.example.habittracker

import android.os.Parcelable
import com.example.habittracker.enums.Priority
import com.example.habittracker.enums.Type
import kotlinx.android.parcel.Parcelize

@Parcelize
class Habit(
    var Name: String,
    var Description: String,
    var Priority: Priority,
    var Type: Type,
    var Times: Int,
    var Period: Int
): Parcelable {
}