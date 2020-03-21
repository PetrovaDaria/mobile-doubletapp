package com.example.habittracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class EditHabitActivity: AppCompatActivity() {
    private val habitPriorities = mapOf(
        Priority.Low to 0,
        Priority.Medium to 1,
        Priority.High to 2
    )

    private val priorityIndexToEnum = mapOf(
        "Низкий" to Priority.Low,
        "Средний" to Priority.Medium,
        "Высокий" to Priority.High
    )

    private val habitTypeToId = mapOf (
        Type.Good to R.id.habit_type_good,
        Type.Bad to R.id.habit_type_bad
    )

    private val typeIndexToEnum = mapOf(
        0 to Type.Good,
        1 to Type.Bad
    )

    private var habitPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_habit)
        val habit = intent.getParcelableExtra<Habit>("habit")
        val habitPosition = intent.getIntExtra("habitPosition", -1)
        this.habitPosition = habitPosition

        if (habit != null) {
            val habitName = findViewById<EditText>(R.id.habit_name_edit)
            habitName.setText(habit.Name)

            val habitDescription = findViewById<EditText>(R.id.habit_description_edit)
            habitDescription.setText(habit.Description)

            val habitPriority = findViewById<Spinner>(R.id.habit_priority_edit)
            val selection = habitPriorities[habit.Priority]
            if (selection != null) {
                habitPriority.setSelection(selection)
            }

            val habitTypeId = habitTypeToId[habit.Type] ?: R.id.habit_type_good
            val habitType = findViewById<RadioButton>(habitTypeId)
            habitType.isChecked = true

            val habitTimes = findViewById<EditText>(R.id.habit_times_edit)
            habitTimes.setText(habit.Times.toString())

            val habitPeriod = findViewById<EditText>(R.id.habit_period_edit)
            habitPeriod.setText(habit.Period.toString())
        }
    }

    fun onSave(view: View) {
        val name = findViewById<EditText>(R.id.habit_name_edit)
        val description = findViewById<EditText>(R.id.habit_description_edit)
        val priority = findViewById<Spinner>(R.id.habit_priority_edit)
        val type = findViewById<RadioGroup>(R.id.habit_type_edit)
        val times = findViewById<EditText>(R.id.habit_times_edit)
        val period = findViewById<EditText>(R.id.habit_period_edit)

        val priorityValue = priorityIndexToEnum[priority.selectedItem.toString()] ?: Priority.High;

        val typeValue = if (type.checkedRadioButtonId == R.id.habit_type_good) {
            Type.Good
        } else {
            Type.Bad
        }

        val habit = Habit(
            name.text.toString() ?: "",
            description.text.toString() ?: "",
            priorityValue ?: Priority.High,
            typeValue ?: Type.Good,
            times?.text?.toString()?.toInt() ?: 1,
            period?.text?.toString()?.toInt() ?: 1
        )

        val intent = Intent()
        intent.putExtra("habit", habit)
        intent.putExtra("habitPosition", habitPosition);
        setResult(RESULT_OK, intent)
        finish()
    }
}