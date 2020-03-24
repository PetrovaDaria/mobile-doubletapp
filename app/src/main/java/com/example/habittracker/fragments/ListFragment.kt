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
    private var habits = mutableListOf(
        Habit(
            "Спать 8 часов",
            "Минимум 7, максимум 9",
            Priority.High,
            Type.Good,
            1,
            1
        ),
        Habit(
            "Пить вино",
            "И ничего крепче",
            Priority.Low,
            Type.Bad,
            1,
            7
        )
    )

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

        return view
    }

    fun onEditHabit(v: View?) {
        if (v != null) {
            val position = viewManager.getPosition(v)
            val habit = (viewAdapter as DataAdapter).getHabit(position)
            callback?.onEditHabit(habit, position)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList("habits", ArrayList<Parcelable>(habits));
    }
}

interface ListCallback {
    fun onAddHabit()

    fun onEditHabit(habit: Habit, habitPosition: Int)
}