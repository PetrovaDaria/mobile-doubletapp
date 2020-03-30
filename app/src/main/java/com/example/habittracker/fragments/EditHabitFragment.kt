package com.example.habittracker.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.habittracker.Habit
import com.example.habittracker.enums.Priority
import com.example.habittracker.R
import com.example.habittracker.enums.Type

class EditHabitFragment: Fragment() {
    private var habitPosition = -1
    var callback: EditHabitCallback? = null

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

    companion object {
        fun newInstance(habit: Habit, habitPosition: Int): EditHabitFragment {
            val fragment = EditHabitFragment()
            val bundle = Bundle()
            bundle.putInt("habitPosition", habitPosition)
            bundle.putParcelable("habit", habit)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = activity as EditHabitCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_habit_fragment, container, false)
        var habit: Habit? = null
        var habitPosition = -1
        arguments?.let {
            habit = it.getParcelable("habit")
            habitPosition = it.getInt("habitPosition")
        }
        this.habitPosition = habitPosition

        if (habit != null) {
            val habitName = view.findViewById<EditText>(R.id.habit_name_edit)
            habitName.setText(habit!!.Name)

            val habitDescription = view.findViewById<EditText>(R.id.habit_description_edit)
            habitDescription.setText(habit!!.Description)

            val habitPriority = view.findViewById<Spinner>(R.id.habit_priority_edit)
            val selection = habitPriorities[habit!!.Priority]
            if (selection != null) {
                habitPriority.setSelection(selection)
            }

            val habitTypeId = habitTypeToId[habit!!.Type] ?: R.id.habit_type_good
            val habitType = view.findViewById<RadioButton>(habitTypeId)
            habitType.isChecked = true

            val habitTimes = view.findViewById<EditText>(R.id.habit_times_edit)
            habitTimes.setText(habit!!.Times.toString())

            val habitPeriod = view.findViewById<EditText>(R.id.habit_period_edit)
            habitPeriod.setText(habit!!.Period.toString())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fun onSave() {
            val name = view.findViewById<EditText>(R.id.habit_name_edit)
            val description = view.findViewById<EditText>(R.id.habit_description_edit)
            val priority = view.findViewById<Spinner>(R.id.habit_priority_edit)
            val type = view.findViewById<RadioGroup>(R.id.habit_type_edit)
            val times = view.findViewById<EditText>(R.id.habit_times_edit)
            val period = view.findViewById<EditText>(R.id.habit_period_edit)

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

            callback?.onSaveHabit(habit, habitPosition)
        }

        val saveBtn = view.findViewById<Button>(R.id.habit_save)
        saveBtn.setOnClickListener {
            onSave()
        }
    }
}

interface EditHabitCallback {
    fun onSaveHabit(habit: Habit, habitPosition: Int)
}