package com.example.habittracker

import android.os.Parcelable
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