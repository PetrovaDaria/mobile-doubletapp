package com.example.habittracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.models.Habit
import com.example.habittracker.db.HabitDatabase
import com.example.habittracker.enums.Priority
import com.example.habittracker.R
import com.example.habittracker.enums.Type
import com.example.habittracker.viewModels.EditHabitViewModel

class EditHabitFragment: Fragment() {
    private var habitId: Int? = null
    var callback: EditHabitCallback? = null
    private lateinit var viewModel: EditHabitViewModel

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
        fun newInstance(habitId: Int): EditHabitFragment {
            val fragment = EditHabitFragment()
            val bundle = Bundle()
            bundle.putInt("habitId", habitId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = activity as EditHabitCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return EditHabitViewModel(HabitDatabase.getInstance(activity!!.applicationContext)) as T
            }
        }).get(EditHabitViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_habit_fragment, container, false)
        arguments?.let {
            habitId = it.getInt("habitId")
        }

        if (habitId != null) {
            val habit = viewModel.getHabitById(habitId!!)

            val habitName = view.findViewById<EditText>(R.id.habit_name_edit)
            habitName.setText(habit.Name)

            val habitDescription = view.findViewById<EditText>(R.id.habit_description_edit)
            habitDescription.setText(habit.Description)

            val habitPriority = view.findViewById<Spinner>(R.id.habit_priority_edit)
            val selection = habitPriorities[habit.Priority]
            if (selection != null) {
                habitPriority.setSelection(selection)
            }

            val habitTypeId = habitTypeToId[habit.Type] ?: R.id.habit_type_good
            val habitType = view.findViewById<RadioButton>(habitTypeId)
            habitType.isChecked = true

            val habitTimes = view.findViewById<EditText>(R.id.habit_times_edit)
            habitTimes.setText(habit.Times.toString())

            val habitPeriod = view.findViewById<EditText>(R.id.habit_period_edit)
            habitPeriod.setText(habit.Period.toString())
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

            val habit: Habit?
            if (habitId == null) {
                habit = Habit(
                    name.text.toString() ?: "",
                    description.text.toString() ?: "",
                    priorityValue ?: Priority.High,
                    typeValue ?: Type.Good,
                    times?.text?.toString()?.toInt() ?: 1,
                    period?.text?.toString()?.toInt() ?: 1
                )
            } else {
                habit = Habit(
                    name.text.toString() ?: "",
                    description.text.toString() ?: "",
                    priorityValue ?: Priority.High,
                    typeValue ?: Type.Good,
                    times?.text?.toString()?.toInt() ?: 1,
                    period?.text?.toString()?.toInt() ?: 1,
                    habitId!!
                )
            }

            if (habitId == null) {
                viewModel.addHabit(habit)
            } else {
                viewModel.editHabit(habit)
            }
            callback?.onSaveHabit()
        }

        val saveBtn = view.findViewById<Button>(R.id.habit_save)
        saveBtn.setOnClickListener {
            onSave()
        }
    }
}

interface EditHabitCallback {
    fun onSaveHabit()
}