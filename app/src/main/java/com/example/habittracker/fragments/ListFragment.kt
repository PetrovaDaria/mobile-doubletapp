package com.example.habittracker.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.*
import com.example.habittracker.enums.Priority
import com.example.habittracker.enums.Type
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment: Fragment() {
    var callback: ListCallback? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var filterType: Type
//    private var habits = mutableListOf(
//        Habit(
//            "Спать 8 часов",
//            "Минимум 7, максимум 9",
//            Priority.High,
//            Type.Good,
//            1,
//            1
//        ),
//        Habit(
//            "Пить вино",
//            "И ничего крепче",
//            Priority.Low,
//            Type.Bad,
//            1,
//            7
//        )
//    )
    private var habits = mutableListOf<Habit>()
    private var filteredHabits = mutableListOf<Habit>()
    private var habitsPositions = mutableMapOf<Habit, Int>()

    companion object {
        fun newInstance(type: Type, habits: List<Habit>): ListFragment {
            val fragment = ListFragment()
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            bundle.putParcelableArrayList("habits", ArrayList<Parcelable>(habits))
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = activity as ListCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            habits = savedInstanceState.getParcelableArrayList<Parcelable>("habits") as MutableList<Habit>
        }

        viewAdapter = DataAdapter(habits)
        (viewAdapter as DataAdapter).setOnItemClickListener(object: View.OnClickListener {

            override fun onClick(v: View?) {
                onEditHabit(v)
            }
        })

//        arguments?.let {
//            filterType = it.getSerializable("type") as Type
//            habits = it.getParcelableArrayList<Parcelable>("habits") as MutableList<Habit>
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        viewManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.habits_list_recycler_view)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            callback?.onAddHabit()
        }

        var habit: Habit? = null
        var habitPosition = -1
        arguments?.let {
            habit = it.getParcelable("habit")
            habitPosition = it.getInt("habitPosition")
        }
        println("habit position")
        println(habitPosition)
        if (habit != null) {
            if (habitPosition == -1) {
                habits.add(habit!!)
            }
            else {
                habits[habitPosition] = habit!!
                viewAdapter.notifyItemChanged(habitPosition)
            }
        }
        arguments?.clear()

        // filterHabits()

        return view
    }

    fun onEditHabit(v: View?) {
        if (v != null) {
            val position = viewManager.getPosition(v)
            val habit = (viewAdapter as DataAdapter).getHabit(position)
            callback?.onEditHabit(habit, position)
//            val realPosition = habitsPositions[habit]
//            if (realPosition != null) {
//                callback?.onEditHabit(habit, realPosition)
//            }
        }
    }

//    fun filterHabits() {
//        for (i in 0 until habits.size) {
//            val habit = habits[i]
//            if (habit.Type == filterType) {
//                filteredHabits.add(habit)
//                habitsPositions[habit] = i
//            }
//        }
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList("habits", ArrayList<Parcelable>(habits));
    }
}

interface ListCallback {
    fun onAddHabit()

    fun onEditHabit(habit: Habit, habitPosition: Int)
}